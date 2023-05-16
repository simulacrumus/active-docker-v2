package com.example.active.business.domain.enums.converter;

import com.example.active.business.domain.enums.FacilitySortEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FacilitySortEnumConverter implements Converter<String, FacilitySortEnum> {
    @Override
    public FacilitySortEnum convert(String source) {
        return FacilitySortEnum.of(source);
    }
}