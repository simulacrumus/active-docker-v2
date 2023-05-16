package com.example.active.business.domain.paramobject;

import com.example.active.business.domain.enums.CityFilterEnum;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.business.domain.enums.TypeSortEnum;

import java.util.Optional;

public class CategoryParams extends PageParams implements ParamDefault{

    private Long facilityId;
    private Long categoryId;

    public CategoryParams() {
        super();
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public CategoryParams facilityId(Long facilityId){
        setFacilityId(facilityId);
        return this;
    }

    public CategoryParams categoryId(Long categoryId){
        setCategoryId(categoryId);
        return this;
    }

    public CategoryParams pageNumber(Optional<Integer> pageNumber){
        setPageNumber(pageNumber.orElse(DEFAULT_PAGE_NUMBER));
        return this;
    }

    public CategoryParams pageSize(Optional<Integer> pageSize){
        setPageSize(pageSize.orElse(DEFAULT_PAGE_SIZE));
        return this;
    }

    public CategoryParams query(Optional<String> query){
        setQuery(query.orElse(EMPTY_STRING));
        return this;
    }

    public CategoryParams language(Optional<LanguageFilterEnum> languageFilterEnum){
        setLanguage(languageFilterEnum.orElse(LanguageFilterEnum.EN));
        return this;
    }

    public CategoryParams city(CityFilterEnum cityFilterEnum){
        setCity(cityFilterEnum.getValue());
        return this;
    }

    public static CategoryParams builder(){
        return new CategoryParams();
    }
}
