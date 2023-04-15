package com.zxchen.nearby.order.domain.vo;

import java.util.List;

/**
 * 向前端返回商品列表的VO
 */
public class CommodityListVo {

    // 返回商品列表长度
    private int length;

    private List<CommodityVo> list;

    public CommodityListVo(List<CommodityVo> voList) {
        this.list = voList;
        this.length = voList.size();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<CommodityVo> getList() {
        return list;
    }

    public void setList(List<CommodityVo> list) {
        this.list = list;
    }

}
