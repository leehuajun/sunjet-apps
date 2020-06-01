package com.sunjet.utils.dto;

import com.sunjet.utils.dto.custom.CustomCriterion;
import com.sunjet.utils.dto.custom.SearchOrder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询封装 查询模板
 * <p>
 * specification 和 customCriterion 的排他性（优先使用 specification ）:
 * 如果 specification 为 null, 则以 customCriterion 进行计算出 specification,
 * 否则 系统会忽略 customCriterion, 直接使用 specification
 * <p>
 * Created by hxf on 2015-11-18.
 */
public class SearchDTO<T> {
    private Integer pageNumber;//页码
    private Integer pageSize;//每页记录数
    private List<SearchOrder> searchOrderList = new ArrayList<>();//排序集合
    private Specification<T> specification = null;  //查询条件
    private CustomCriterion customCriterion;//查询条件

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<SearchOrder> getSearchOrderList() {
        return searchOrderList;
    }

    public void setSearchOrderList(List<SearchOrder> searchOrderList) {
        this.searchOrderList = searchOrderList;
    }

    public Specification<T> getSpecification() {
        return specification;
    }

    public void setSpecification(Specification<T> specification) {
        this.specification = specification;
    }

    public CustomCriterion getCustomCriterion() {
        return customCriterion;
    }

    public void setCustomCriterion(CustomCriterion customCriterion) {
        this.customCriterion = customCriterion;
    }
}
