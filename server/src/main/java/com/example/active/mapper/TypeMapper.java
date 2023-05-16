package com.example.active.mapper;

import com.example.active.business.domain.TypeDTO;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.data.entity.main.Type;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TypeMapper {
    public TypeDTO toDTO(Type type, LanguageFilterEnum language){
        return TypeDTO.builder()
                .id(type.getId())
                .title(type.getTitleTranslation().getTranslations().get(language.getValue()))
                .category(type.getCategory().getTitleTranslation().getTranslations().get(language.getValue()))
                .build();
    }
}
