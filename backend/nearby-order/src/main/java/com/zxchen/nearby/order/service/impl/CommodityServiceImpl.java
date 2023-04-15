package com.zxchen.nearby.order.service.impl;

import com.zxchen.nearby.common.constant.StatusCode;
import com.zxchen.nearby.common.domain.cache.UserInfoCache;
import com.zxchen.nearby.common.exception.CustomException;
import com.zxchen.nearby.common.redis.RedisCache;
import com.zxchen.nearby.common.service.CacheService;
import com.zxchen.nearby.common.util.StringUtil;
import com.zxchen.nearby.order.constant.OrderConstants;
import com.zxchen.nearby.order.domain.dao.CommodityDao;
import com.zxchen.nearby.order.domain.dao.CommodityDetailDao;
import com.zxchen.nearby.order.domain.dao.CommodityLikeDao;
import com.zxchen.nearby.order.domain.dao.CommodityListDao;
import com.zxchen.nearby.order.domain.dto.CommodityCache;
import com.zxchen.nearby.order.domain.dto.CommodityFindDto;
import com.zxchen.nearby.order.domain.dto.CommoditySellDto;
import com.zxchen.nearby.order.domain.dto.OnSellListDto;
import com.zxchen.nearby.order.domain.vo.CommodityDetailVo;
import com.zxchen.nearby.order.domain.vo.CommodityTradeVo;
import com.zxchen.nearby.order.domain.vo.CommodityVo;
import com.zxchen.nearby.order.mapper.CommodityMapper;
import com.zxchen.nearby.order.service.ICollectionService;
import com.zxchen.nearby.order.service.ICommodityService;
import com.zxchen.nearby.order.util.builder.CommodityDaoBuilder;
import com.zxchen.nearby.order.util.builder.CommodityDetailVoBuilder;
import com.zxchen.nearby.order.util.builder.CommodityVoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 商品发布 服务层实现
 */
@Service
public class CommodityServiceImpl implements ICommodityService {

    private RedisCache redisCache;

    private CommodityMapper commodityMapper;

    private CacheService cacheService;

    private ICollectionService collectionService;

    @Value("${qcloud.mysql.ngram-token-size}")
    private int ngramTokenSize;

    /**
     * 发布商品
     *
     * @param dto 存储了卖家ID，商品信息，交易地点信息等内容{@link CommoditySellDto}
     * @return 发布操作是否成功
     */
    @Override
    public boolean sell(CommoditySellDto dto) {
        CommodityDao dao = CommodityDaoBuilder.build(dto);
        commodityMapper.insertCommodity(dao);
        Long cid = dao.getCid();
        CommodityCache cache = commodityMapper.selectCommodityCache(cid);
        redisCache.addGeoPoint(OrderConstants.REDIS_GEO_KEY,
                dto.getLatitude(), dto.getLongitude(),
                cid.toString());
        redisCache.setCacheObject(OrderConstants.REDIS_COMMODITY_PREFIX + cid,
                cache);
        return true;
    }

    /**
     * 查看附近正在出售的商品
     *
     * @param dto 包括用户ID以及用户所处位置
     * @return
     */
    @Override
    public List<CommodityVo> find(CommodityFindDto dto) {
        Integer radius = dto.getRadius();
        // 调用 geo radius 命令
        GeoResults<GeoLocation<String>> geoResults =
                redisCache.geoRadiusAsc(OrderConstants.REDIS_GEO_KEY,
                        dto.getLatitude(), dto.getLongitude(),
                        radius == null ? OrderConstants.DEFAULT_RADIUS_METERS : radius.intValue());
        List<CommodityVo> voList = new ArrayList<>();
        Iterator<GeoResult<GeoLocation<String>>> iterator = geoResults.iterator();
        iterator.forEachRemaining((GeoResult<GeoLocation<String>> result) -> {
            String cid = result.getContent().getName();
            CommodityCache cache = redisCache.getCacheObject(OrderConstants.REDIS_COMMODITY_PREFIX + cid);
            if (!checkOwnCommodity(cache, dto.getUid())) {
                UserInfoCache userInfo = cacheService.getUserInfoFromCache(cache.getSellerId());
                CommodityVo vo = CommodityVoBuilder.build(cache, userInfo, result.getDistance());
                voList.add(vo);
            }
        });
        return voList;
    }

    /**
     * 通过关键词搜索附近正在出售的商品
     *
     * @param dto 包括用户ID以及用户所处位置，以及用户搜索的关键词
     * @return
     */
    @Override
    public List<CommodityVo> search(CommodityFindDto dto) {
        List<String> keywords = StringUtil.splitBySpace(dto.getKeyword());
        for (String keyword: keywords) {
            // 因此如果关键字长度小于ngram的最小分词大小，则通过like方法访问数据库
            if (keyword.length() < ngramTokenSize) {
                return searchByLike(dto, keywords);
            }
        }
        // 若分词都至少有两个字，通过match方法访问数据库（可利用全文索引）
        return searchByMatch(dto);
    }

    /**
     * 获取商品的详细信息
     *
     * @param cid 商品ID
     * @param uid 提交请求的用户ID
     * @return
     */
    @Override
    public CommodityDetailVo detail(Long cid, Long uid) {
        CommodityDetailDao dao = commodityMapper.selectCommodityDetail(cid);
        if (dao == null) {
            throw new CustomException("未找到该商品", StatusCode.NOT_FOUND);
        }
        boolean isCollected = collectionService.checkCollected(uid, cid);
        CommodityDetailVo vo = CommodityDetailVoBuilder.build(dao, uid, isCollected);
        return vo;
    }

    /**
     * 查看正在出售的商品列表
     *
     * @param dto 包含要查看的用户UID，以及查看者当前的位置信息
     * @return
     */
    @Override
    public List<CommodityVo> onSellList(OnSellListDto dto) {
        List<CommodityListDao> daoList = commodityMapper.selectOnSellList(dto);
        List<CommodityVo> voList = new ArrayList<>();
        for (CommodityListDao dao: daoList) {
            CommodityVo vo = CommodityVoBuilder.build(dao);
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 获取缓存的商品信息
     *
     * @param cid 商品ID
     * @return 商品信息
     */
    @Override
    public CommodityCache getCommodityCacheInfo(Long cid) {
        String key = OrderConstants.REDIS_COMMODITY_PREFIX + cid;
        CommodityCache cache = redisCache.getCacheObject(key);
        if (cache == null) {
            cache = commodityMapper.selectCommodityCache(cid);
            redisCache.setCacheObject(key, cache);
        }
        return cache;
    }

    /**
     * 将商品状态设为上架
     *
     * @param cid 商品ID
     */
    @Override
    public void putCommodityOnSell(Long cid) {
        // 更新数据库中的状态信息
        commodityMapper.updateCommodityStatusToOnSell(cid);
        // 将当前商品加入回redis缓存当中
        CommodityCache cache = commodityMapper.selectCommodityCache(cid);
        CommodityDetailDao commodityDetail = commodityMapper.selectCommodityDetail(cid);
        redisCache.addGeoPoint(OrderConstants.REDIS_GEO_KEY,
                commodityDetail.getLatitude(), commodityDetail.getLongitude(),
                cid.toString());
        redisCache.setCacheObject(OrderConstants.REDIS_COMMODITY_PREFIX + cid,
                cache);
    }

    /**
     * 将商品状态设为下架
     *
     * @param cid 商品ID
     */
    @Override
    public void putCommodityOffShelf(Long cid) {
        // 更新数据库中的状态信息
        commodityMapper.updateCommodityStatusToOffShelf(cid);
        // 将当前商品从redis缓存中删除
        redisCache.deleteObjectFromZSet(OrderConstants.REDIS_GEO_KEY, cid.toString());
        redisCache.deleteObject(OrderConstants.REDIS_COMMODITY_PREFIX + cid);
        // 取消与当前商品有关的收藏
        collectionService.cancelCommodityCollection(cid);
    }

    /**
     * 校验商品是否处于商家状态
     *
     * @param cid 商品ID
     * @return 商品是否处于上架状态
     */
    @Override
    public boolean isCommodityOnSell(Long cid) {
        return commodityMapper.checkCommodityIsOnSell(cid) > 0;
    }

    /**
     * 校验商品是否可以进行交换
     *
     * @param cid 商品ID
     * @return 商品是否可以进行交换
     */
    @Override
    public boolean commodityCouldExchange(Long cid) {
        return commodityMapper.checkCommodityCouldExchange(cid) > 0;
    }

    /**
     * 编辑商品信息
     *
     * @param cid 商品ID
     * @param dto 存储了卖家ID，商品信息，交易地点信息等内容{@link CommoditySellDto}
     * @return 编辑操作是否成功
     */
    @Override
    public boolean edit(Long cid, CommoditySellDto dto) {
        // 校验当前操作者是否是商品的卖家
        CommodityDetailDao commodityDetail = commodityMapper.selectCommodityDetail(cid);
        if (!commodityDetail.getSellerId().equals(dto.getSellerId())) {
            throw new CustomException("用户不是商品卖家，无操作权限", StatusCode.FORBIDDEN);
        }

        // 更新数据库中的商品信息
        CommodityDao dao = CommodityDaoBuilder.build(cid, dto);
        commodityMapper.updateCommodity(dao);
        // 更新缓存中的商品信息
        redisCache.deleteObjectFromZSet(OrderConstants.REDIS_GEO_KEY, cid.toString());
        redisCache.addGeoPoint(OrderConstants.REDIS_GEO_KEY,
                dto.getLatitude(), dto.getLongitude(),
                cid.toString());
        CommodityCache cache = commodityMapper.selectCommodityCache(cid);
        redisCache.setCacheObject(OrderConstants.REDIS_COMMODITY_PREFIX + cid,
                cache);
        return true;
    }

    /**
     * 获取下单界面显示的商品简略信息
     *
     * @param cid 商品ID
     * @return 商品的ID、描述、图片，以及是否支持交换
     */
    @Override
    public CommodityTradeVo getTradeSimpleInfo(Long cid) {
        return commodityMapper.selectTradePageInfo(cid);
    }

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Autowired
    public void setCollectionService(ICollectionService collectionService) {
        this.collectionService = collectionService;
    }

    /**
     * 校验是否是用户自己的商品
     */
    private boolean checkOwnCommodity(CommodityCache cache, Long uid) {
        Long id = cache.getSellerId();
        return id.equals(uid);
    }

    /**
     * 通过 Like 的形式进行关键词搜索
     *
     * @param dto  包含用户的ID、位置、搜索半径等
     * @param keywords 用于搜索的关键词列表
     * @return 返回给前端进行展示的商品列表
     */
    private List<CommodityVo> searchByLike(CommodityFindDto dto, List<String> keywords) {
        int radius = dto.getRadius() == null ? OrderConstants.DEFAULT_RADIUS_METERS : dto.getRadius();
        CommodityLikeDao dao = new CommodityLikeDao(dto.getUid(), dto.getLatitude(),
                dto.getLongitude(), radius, keywords);
        List<CommodityListDao> daoList = commodityMapper.selectCommodityListByLike(dao);
        return transferDaoToVo(daoList);
    }

    /**
     * 通过 Match 的形式进行关键词搜索
     * @param dto 包含用户的ID、位置、搜索半径、搜索关键词
     * @return 返回给前端进行展示的商品列表
     */
    private List<CommodityVo> searchByMatch(CommodityFindDto dto) {
        // 检查用户是否设置了搜索半径，未设置则使用默认值
        int radius = dto.getRadius() == null ? OrderConstants.DEFAULT_RADIUS_METERS : dto.getRadius();
        dto.setRadius(radius);
        List<CommodityListDao> daoList = commodityMapper.selectCommodityListByMatch(dto);
        return transferDaoToVo(daoList);
    }

    /**
     * 将数据库返回的 DAO 列表转换为 VO 列表，以便返回给前端
     *
     * @param daoList 数据库返回的DAO列表
     * @return 转换得到的VO列表
     */
    private List<CommodityVo> transferDaoToVo(List<CommodityListDao> daoList) {
        List<CommodityVo> voList = new ArrayList<>();
        daoList.forEach((CommodityListDao resLine) -> {
            CommodityVo vo = CommodityVoBuilder.build(resLine);
            voList.add(vo);
        });
        return voList;
    }

}
