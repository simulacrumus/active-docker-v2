package com.example.active.business.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeSortEnum {
    TITLE("title"), POPULAR("popular"), DEFAULT("default");

    private String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TypeSortEnum of(String value) {
        if (null == value || value.isEmpty()) {
            return null;
        }
        for (TypeSortEnum item : TypeSortEnum.values()) {
            if (value.equals(item.getValue())) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unknown sort option: " + value);
    }

    TypeSortEnum(String value) {
        this.value = value;
    }
}