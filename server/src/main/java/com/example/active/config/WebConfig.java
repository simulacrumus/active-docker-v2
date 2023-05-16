package com.example.active.config;

import com.example.active.business.domain.enums.converter.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ActivitySortEnumConverter());
        registry.addConverter(new LanguageFilterEnumConverter());
        registry.addConverter(new FacilitySortEnumConverter());
        registry.addConverter(new CityFilterEnumConverter());
        registry.addConverter(new TypeSortEnumConverter());
    }
}