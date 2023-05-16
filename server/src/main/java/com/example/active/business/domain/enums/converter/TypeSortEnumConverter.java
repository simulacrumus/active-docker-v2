package com.example.active.business.domain.enums.converter;

import com.example.active.business.domain.enums.TypeSortEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TypeSortEnumConverter implements Converter<String, TypeSortEnum> {
    @Override
    public TypeSortEnum convert(String source) {
        return TypeSortEnum.of(source);
    }
}
