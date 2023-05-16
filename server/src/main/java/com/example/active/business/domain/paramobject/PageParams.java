package com.example.active.business.domain.paramobject;

import com.example.active.business.domain.enums.CityFilterEnum;

public abstract class PageParams extends Params{
    public PageParams(){
        super();
    }

    private Integer pageNumber;
    private Integer pageSize;
    private String query;
//    private String sort;

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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query.trim();
    }

//    public String getSort() {
//        return sort;
//    }
//
//    public void setSort(String sort) {
//        this.sort = sort;
//    }
}
