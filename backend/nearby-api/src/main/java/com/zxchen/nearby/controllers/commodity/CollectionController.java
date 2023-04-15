package com.zxchen.nearby.controllers.commodity;

import com.zxchen.nearby.common.domain.web.HttpResult;
import com.zxchen.nearby.common.util.SecurityUtil;
import com.zxchen.nearby.order.domain.dto.CollectionListDto;
import com.zxchen.nearby.order.domain.body.CollectBody;
import com.zxchen.nearby.order.domain.vo.CommodityListVo;
import com.zxchen.nearby.order.domain.vo.CommodityVo;
import com.zxchen.nearby.order.domain.body.LocationBody;
import com.zxchen.nearby.order.service.ICollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏相关操作的接口
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {

    private ICollectionService collectionService;

    /**
     * 添加收藏接口
     *
     * @param body 请求体，包含字段 "cid": 要收藏的商品ID
     * @return 若收藏成功，返回200
     */
    @PostMapping("/add")
    public HttpResult add(@RequestBody CollectBody body) {
        Long uid = SecurityUtil.getUserId();
        collectionService.addCollection(uid, body.getCid());
        return HttpResult.success();
    }

    /**
     * 取消收藏接口
     *
     * @param body 请求体，包含商品CID
     * @return
     */
    @PostMapping("/cancel")
    public HttpResult cancel(@RequestBody CollectBody body) {
        Long uid = SecurityUtil.getUserId();
        collectionService.cancelCollection(uid, body.getCid());
        return HttpResult.success();
    }

    /**
     * 获取收藏列表的接口
     *
     * @param body 请求体，包含用户当前所处位置坐标
     * @return 收藏商品列表 {@link CommodityListVo}
     */
    @RequestMapping("/list")
    public HttpResult list(@RequestBody LocationBody body) {
        Long uid = SecurityUtil.getUserId();
        List<CommodityVo> voList = collectionService
                .collectionList(new CollectionListDto(uid, body.getLatitude(), body.getLongitude()));
        return HttpResult.success(new CommodityListVo(voList));
    }

    @Autowired
    public void setCollectionService(ICollectionService collectionService) {
        this.collectionService = collectionService;
    }
}
