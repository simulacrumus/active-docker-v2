package com.example.active.business.domain.paramobject;

import com.example.active.business.domain.enums.CityFilterEnum;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.business.domain.enums.TypeSortEnum;

import java.util.Optional;

public class TypeParams extends PageParams implements ParamDefault{

    public TypeParams() {
    }

    private Long typeId;
    private Long facilityId;
    private Long categoryId;

    private TypeSortEnum sortEnum;

    public TypeSortEnum getSortEnum() {
        return sortEnum;
    }
    public void setSortEnum(TypeSortEnum sortEnum) {
        this.sortEnum = sortEnum;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
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

    public TypeParams typeId(Long typeId){
        setTypeId(typeId);
        return this;
    }

    public TypeParams facilityId(Long facilityId){
        setFacilityId(facilityId);
        return this;
    }

    public TypeParams categoryId(Long categoryId){
        setCategoryId(categoryId);
        return this;
    }

    public TypeParams pageNumber(Optional<Integer> pageNumber){
        setPageNumber(pageNumber.orElse(DEFAULT_PAGE_NUMBER));
        return this;
    }

    public TypeParams pageSize(Optional<Integer> pageSize){
        setPageSize(pageSize.orElse(DEFAULT_PAGE_SIZE));
        return this;
    }

    public TypeParams sort(Optional<TypeSortEnum> sortEnum){
        setSortEnum(sortEnum.orElse(TypeSortEnum.DEFAULT));
        return this;
    }

    public TypeParams query(Optional<String> query){
        setQuery(query.orElse(EMPTY_STRING));
        return this;
    }

    public TypeParams language(Optional<LanguageFilterEnum> languageFilterEnum){
        setLanguage(languageFilterEnum.orElse(LanguageFilterEnum.EN));
        return this;
    }

    public TypeParams city(CityFilterEnum cityFilterEnum){
        setCity(cityFilterEnum.getValue());
        return this;
    }

    public static TypeParams builder(){
        return new TypeParams();
    }
}
