package com.example.active.business.domain.paramobject;

import com.example.active.business.domain.enums.ActivitySortEnum;
import com.example.active.business.domain.enums.CityFilterEnum;
import com.example.active.business.domain.enums.FacilitySortEnum;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import java.util.Optional;

public class FacilityParams extends PageParams implements ParamDefault{
    private FacilitySortEnum sortEnum;
    private Double longitude;
    private Double latitude;

    private Long facilityId;
    private Long activityId;
    private Long typeId;

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public FacilitySortEnum getSortEnum() {
        return sortEnum;
    }

    public void setSortEnum(FacilitySortEnum sortEnum) {
        this.sortEnum = sortEnum;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public FacilityParams typeId(Long typeId){
        setTypeId(typeId);
        return this;
    }

    public FacilityParams longitude(Optional<Double> longitude){
        setLongitude(longitude.orElse(DEFAULT_LONGITUDE));
        return this;
    }

    public FacilityParams latitude(Optional<Double> latitude){
        setLatitude(latitude.orElse(DEFAULT_LATITUDE));
        return this;
    }

    public FacilityParams activityId (Long activityId){
        setActivityId(activityId);
        return this;
    }

    public FacilityParams facilityId(Long facilityId){
        setFacilityId(facilityId);
        return this;
    }

    public FacilityParams pageNumber(Optional<Integer> pageNumber){
        setPageNumber(pageNumber.orElse(DEFAULT_PAGE_NUMBER));
        return this;
    }

    public FacilityParams pageSize(Optional<Integer> pageSize){
        setPageSize(pageSize.orElse(DEFAULT_PAGE_SIZE));
        return this;
    }

    public FacilityParams sort(Optional<FacilitySortEnum> sortEnum){
        setSortEnum(sortEnum.orElse(FacilitySortEnum.DEFAULT));
        return this;
    }

    public FacilityParams query(Optional<String> query){
        setQuery(query.orElse(EMPTY_STRING));
        return this;
    }

    public FacilityParams language(Optional<LanguageFilterEnum> languageFilterEnum){
        setLanguage(languageFilterEnum.orElse(LanguageFilterEnum.EN));
        return this;
    }

    public FacilityParams city(CityFilterEnum cityFilterEnum){
        setCity(cityFilterEnum.getValue());
        return this;
    }

    public static FacilityParams builder(){
        return new FacilityParams();
    }
}