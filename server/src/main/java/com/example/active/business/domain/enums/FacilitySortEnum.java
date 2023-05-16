package com.example.active.business.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FacilitySortEnum {
    DISTANCE("distance"), TITLE("title"), DEFAULT("default");

    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static FacilitySortEnum of(String value) {
        if (null == value || value.isEmpty()) {
            return null;
        }

        for (FacilitySortEnum item : FacilitySortEnum.values()) {
            if (value.equals(item.getValue())) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unknown sort option: " + value);
    }

    FacilitySortEnum(String value) {
        this.value = value;
    }
}
