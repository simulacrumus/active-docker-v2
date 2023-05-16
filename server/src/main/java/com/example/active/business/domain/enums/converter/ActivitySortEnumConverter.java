package com.example.active.business.domain.enums.converter;

import com.example.active.business.domain.enums.ActivitySortEnum;
import org.springframework.core.convert.converter.Converter;

public class ActivitySortEnumConverter implements Converter<String, ActivitySortEnum> {
    @Override
    public ActivitySortEnum convert(String source) {
        return ActivitySortEnum.of(source);
    }
}