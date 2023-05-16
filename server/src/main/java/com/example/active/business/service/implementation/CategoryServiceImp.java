package com.example.active.business.service.implementation;

import com.example.active.business.domain.CategoryDTO;

import com.example.active.business.domain.paramobject.CategoryParams;
import com.example.active.business.service.CategoryService;
import com.example.active.data.entity.main.Category;
import com.example.active.data.entity.main.City;
import com.example.active.data.entity.main.Facility;
import com.example.active.data.repository.CategoryRepository;
import com.example.active.data.repository.CityRepository;
import com.example.active.data.repository.FacilityRepository;
import com.example.active.data.repository.FacilityTypeRepository;
import com.example.active.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.example.active.util.Utility.MAX_STRING_EDIT_DISTANCE;
import static com.example.active.util.Utility.stringDistanceScore;

@Service
@Qualifier("categoryService")
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private FacilityTypeRepository facilityTypeRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CategoryMapper mapper;

    @Override
    public CategoryDTO findById(CategoryParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        Category category = categoryRepository.findById(params.getCategoryId()).orElseThrow();
        if(!category.getCity().getTitle().equalsIgnoreCase(city.getTitle())){
            throw new NoSuchElementException("Category not found");
        }
        return mapper.toDTO(category, params.getLanguage());
    }

    @Override
    public Page<CategoryDTO> findAll(CategoryParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        List<CategoryDTO> categories = city.getCategories()
                .stream()
                .map(category -> mapper.toDTO(category,params.getLanguage()))
                .filter(categoryDTO -> stringDistanceScore(params.getQuery(), categoryDTO.getTitle()) < MAX_STRING_EDIT_DISTANCE)
                .sorted(Comparator.comparing(CategoryDTO::getTitle))
                .sorted(Comparator.comparing(categoryDTO -> stringDistanceScore(params.getQuery(), categoryDTO.getTitle())))
                .collect(Collectors.toList());
        return listToPage(categories, params.getPageNumber(), params.getPageSize());
    }

    @Override
    public Page<CategoryDTO> findAllByFacility(CategoryParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        Facility facility = facilityRepository.findById(params.getFacilityId()).orElseThrow();
        if(!facility.getCity().getId().equals(city.getId())){
            throw new NoSuchElementException("Facility not found");
        }
        List<CategoryDTO> categories = facility.getFacilityTypes()
                .stream()
                .map(facilityType -> facilityType.getType().getCategory())
                .distinct()
                .map(category -> mapper.toDTO(category,params.getLanguage()))
                .filter(categoryDTO -> stringDistanceScore(params.getQuery(), categoryDTO.getTitle()) < MAX_STRING_EDIT_DISTANCE)
                .sorted(Comparator.comparing(CategoryDTO::getTitle))
                .sorted(Comparator.comparing(categoryDTO -> stringDistanceScore(params.getQuery(), categoryDTO.getTitle())))
                .collect(Collectors.toList());
        return listToPage(categories, params.getPageNumber(), params.getPageSize());
    }

    private Page<CategoryDTO> listToPage(List<CategoryDTO> categories, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), categories.size());
        try{
            return new PageImpl<>(categories.subList(start, end), pageable, categories.size());
        }catch (IllegalArgumentException e){
            return new PageImpl<>(List.of(), pageable, categories.size());
        }
    }
}
