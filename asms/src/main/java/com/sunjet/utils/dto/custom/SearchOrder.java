package com.sunjet.utils.dto.custom;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 查询 排序
 * Created by hxf on 2015-11-18.
 */
public class SearchOrder implements Serializable, Comparator<SearchOrder> {
    public enum OrderType {
        DESC,
        ASC
    }

    private String property;//属性名
    private OrderType orderType;//排序类型：升序、降序
    private Integer seq;//排序先后，值越小越先排序

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public int compare(SearchOrder o1, SearchOrder o2) {
        return o1.getSeq() - o2.getSeq();
    }

    public SearchOrder() {
    }

    public SearchOrder(String property, OrderType orderType, Integer seq) {
        this.property = property;
        this.orderType = orderType;
        this.seq = seq;
    }
}
