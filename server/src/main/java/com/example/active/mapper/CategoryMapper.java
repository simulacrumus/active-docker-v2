package com.example.active.mapper;

import com.example.active.business.domain.ActivityDTO;
import com.example.active.business.domain.CategoryDTO;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.data.entity.main.Availability;
import com.example.active.data.entity.main.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public CategoryDTO toDTO(Category category, LanguageFilterEnum language){
        return CategoryDTO.builder()
                .id(category.getId())
                .title(category.getTitleTranslation().getTranslations().get(language.getValue()))
                .build();
    }

    public Page<CategoryDTO> toDTOPage(Page<Category> page, LanguageFilterEnum language){
        return new PageImpl<>(
                page.getContent().stream().map(availability -> toDTO(availability, language)).collect(Collectors.toList()),
                page.getPageable(),
                page.getTotalElements()
        );
    }
}
