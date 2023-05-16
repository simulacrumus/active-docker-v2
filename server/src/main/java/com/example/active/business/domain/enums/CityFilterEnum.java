package com.example.active.business.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CityFilterEnum {
    OTTAWA("ottawa"), TORONTO("toronto");

    private String value;
    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CityFilterEnum of(String value) {
        if (null == value || value.isEmpty()) {
            return null;
        }

        for (CityFilterEnum item : CityFilterEnum.values()) {
            if (value.equals(item.getValue())) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unknown city option: " + value);
    }

    CityFilterEnum(String value) {
        this.value = value;
    }
}
