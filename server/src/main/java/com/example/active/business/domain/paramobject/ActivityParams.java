package com.example.active.business.domain.paramobject;


import com.example.active.business.domain.enums.ActivitySortEnum;
import com.example.active.business.domain.enums.CityFilterEnum;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.business.domain.enums.TypeSortEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;


public class ActivityParams extends PageParams implements ParamDefault{
    private Optional<Boolean> isAvailable;
    private Optional<LocalDateTime> time;
    private Double longitude;
    private Double latitude;
    private Long facilityId;
    private Long activityId;
    private Long typeId;

    private ActivitySortEnum sortEnum;

    public ActivitySortEnum getSortEnum() {
        return sortEnum;
    }

    public void setSortEnum(ActivitySortEnum sortEnum) {
        this.sortEnum = sortEnum;
    }

    public ActivityParams() {
        super();
    }

    public Optional<Boolean> getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Optional<Boolean> isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Optional<LocalDateTime> getTime() {
        return time;
    }

    public void setTime(Optional<LocalDateTime> time) {
        this.time = time;
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

    public ActivityParams typeId(Long typeId){
        setTypeId(typeId);
        return this;
    }

    public ActivityParams longitude(Optional<Double> longitude){
        setLongitude(longitude.orElse(DEFAULT_LONGITUDE));
        return this;
    }

    public ActivityParams latitude(Optional<Double> latitude){
        setLatitude(latitude.orElse(DEFAULT_LATITUDE));
        return this;
    }

    public ActivityParams isAvailable(Optional<Boolean> isAvailable){
        setIsAvailable(isAvailable);
        return this;
    }

    public ActivityParams time(Optional<LocalDateTime> time){
        setTime(time);
        return this;
    }

    public ActivityParams activityId (Long activityId){
        setActivityId(activityId);
        return this;
    }

    public ActivityParams facilityId(Long facilityId){
        setFacilityId(facilityId);
        return this;
    }

    public ActivityParams pageNumber(Optional<Integer> pageNumber){
        setPageNumber(pageNumber.orElse(DEFAULT_PAGE_NUMBER));
        return this;
    }

    public ActivityParams pageSize(Optional<Integer> pageSize){
        setPageSize(pageSize.orElse(DEFAULT_PAGE_SIZE));
        return this;
    }

    public ActivityParams sort(Optional<ActivitySortEnum> sortEnum){
        setSortEnum(sortEnum.orElse(ActivitySortEnum.DEFAULT));
        return this;
    }

    public ActivityParams query(Optional<String> query){
        setQuery(query.orElse(EMPTY_STRING));
        return this;
    }

    public ActivityParams language(Optional<LanguageFilterEnum> languageFilterEnum){
        setLanguage(languageFilterEnum.orElse(LanguageFilterEnum.EN));
        return this;
    }

    public ActivityParams city(CityFilterEnum cityFilterEnum){
        setCity(cityFilterEnum.getValue());
        return this;
    }

    public static ActivityParams builder(){
        return new ActivityParams();
    }
}
