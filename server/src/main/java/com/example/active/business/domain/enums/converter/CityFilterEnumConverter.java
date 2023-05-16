package com.example.active.business.domain.enums.converter;

import com.example.active.business.domain.enums.CityFilterEnum;
import org.springframework.core.convert.converter.Converter;

public class CityFilterEnumConverter implements Converter<String, CityFilterEnum> {
    @Override
    public CityFilterEnum convert(String source) {
        return CityFilterEnum.of(source);
    }
}