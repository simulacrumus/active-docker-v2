package com.example.active.business.domain.enums.converter;

import com.example.active.business.domain.enums.LanguageFilterEnum;
import org.springframework.core.convert.converter.Converter;

import java.util.Locale;

public class LanguageFilterEnumConverter implements Converter<String, LanguageFilterEnum> {
    @Override
    public LanguageFilterEnum convert(String source) {
        return LanguageFilterEnum.of(source);
    }
}