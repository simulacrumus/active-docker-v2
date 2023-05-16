package com.example.active.business.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActivitySortEnum {
    TIME("time"), DISTANCE("distance"), DEFAULT("default");

    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ActivitySortEnum of(String value) {
        if (null == value || value.isEmpty()) {
            return null;
        }

        for (ActivitySortEnum item : ActivitySortEnum.values()) {
            if (value.equals(item.getValue())) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unknown sort option: " + value);
    }

    ActivitySortEnum(String value) {
        this.value = value;
    }
}
