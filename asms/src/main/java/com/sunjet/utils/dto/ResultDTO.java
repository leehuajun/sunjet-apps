package com.sunjet.utils.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 返回的 通用数据交互类型
 * Created by hxf on 2015-11-18.
 */
public class ResultDTO<T> implements Serializable {
    private long total;//总记录数
    private Integer pageNumber;//当前页码
    private Integer pageSize;//每页显示的记录数
    private Integer numberOfCurrentPage;//当前页记录数
    private List<T> pageContent;//当前页的内容记录集合

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

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

    public Integer getNumberOfCurrentPage() {
        return numberOfCurrentPage;
    }

    public void setNumberOfCurrentPage(Integer numberOfCurrentPage) {
        this.numberOfCurrentPage = numberOfCurrentPage;
    }

    public List<T> getPageContent() {
        return pageContent;
    }

    public void setPageContent(List<T> pageContent) {
        this.pageContent = pageContent;
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "total=" + total +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", numberOfCurrentPage=" + numberOfCurrentPage +
                ", pageContent=" + pageContent +
                '}';
    }
}
