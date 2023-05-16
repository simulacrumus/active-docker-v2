package com.example.active.business.service.implementation;

import com.example.active.business.domain.TypeDTO;
import com.example.active.business.domain.enums.TypeSortEnum;
import com.example.active.business.domain.paramobject.TypeParams;
import com.example.active.business.service.TypeService;
import com.example.active.data.entity.main.*;
import com.example.active.data.repository.*;
import com.example.active.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.active.util.Utility.MAX_STRING_EDIT_DISTANCE;
import static com.example.active.util.Utility.stringDistanceScore;

@Service
@Qualifier("TypeServiceImp")
public class TypeServiceImp implements TypeService {

    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private FacilityTypeRepository facilityTypeRepository;
    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private TypeMapper mapper;

    @Override
    public TypeDTO getById(TypeParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        Type type = typeRepository.findById(params.getTypeId()).orElseThrow();
        if(!city.getId().equals(type.getCategory().getCity().getId())){
            throw new NoSuchElementException("Type not found");
        }
        return mapper.toDTO(type, params.getLanguage());
    }

    @Override
    public Page<TypeDTO> findAll(TypeParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        List<TypeDTO> types = city.getCategories()
                .stream()
                .flatMap(category -> category.getTypes().stream())
                .sorted(getComparatorForType(params.getSortEnum()))
                .map(type -> mapper.toDTO(type,params.getLanguage()))
                .filter(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle()) < MAX_STRING_EDIT_DISTANCE)
                .sorted(Comparator.comparing(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle())))
                .sorted(getComparatorForTypeDTO(params.getSortEnum()))
                .collect(Collectors.toList());
        return listToPage(types, params.getPageNumber(), params.getPageSize());
    }

    @Override
    public Page<TypeDTO> findAllByCategory(TypeParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        Category category = categoryRepository.findById(params.getCategoryId()).orElseThrow();
        if(!city.getId().equals(category.getCity().getId())){
            throw new NoSuchElementException("Category not found");
        }
        List<TypeDTO> types = category.getTypes()
                .stream()
                .sorted(getComparatorForType(params.getSortEnum()))
                .map(type -> mapper.toDTO(type,params.getLanguage()))
                .filter(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle()) < MAX_STRING_EDIT_DISTANCE)
                .sorted(Comparator.comparing(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle())))
                .sorted(getComparatorForTypeDTO(params.getSortEnum()))
                .collect(Collectors.toList());
        return listToPage(types, params.getPageNumber(), params.getPageSize());
    }

    @Override
    public Page<TypeDTO> findAllByFacility(TypeParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        Facility facility = facilityRepository.findById(params.getFacilityId()).orElseThrow();
        if(!city.getId().equals(facility.getCity().getId())){
            throw new NoSuchElementException("Facility not found");
        }
        List<TypeDTO> types = facility.getFacilityTypes()
                .stream()
                .map(FacilityType::getType)
                .sorted(getComparatorForType(params.getSortEnum()))
                .map(type -> mapper.toDTO(type,params.getLanguage()))
                .filter(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle()) < MAX_STRING_EDIT_DISTANCE)
                .sorted(Comparator.comparing(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle())))
                .sorted(getComparatorForTypeDTO(params.getSortEnum()))
                .collect(Collectors.toList());
        return listToPage(types, params.getPageNumber(), params.getPageSize());
    }

    @Override
    public Page<TypeDTO> findAllByFacilityAndCategory(TypeParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        Facility facility = facilityRepository.findById(params.getFacilityId()).orElseThrow();
        Category category = categoryRepository.findById(params.getCategoryId()).orElseThrow();
        if(!city.getId().equals(facility.getCity().getId()) || !city.getId().equals(category.getCity().getId())){
            throw new NoSuchElementException("Not found");
        }
        List<TypeDTO> types = facility.getFacilityTypes()
                .stream()
                .map(FacilityType::getType)
                .filter(type -> type.getCategory().getId().equals(category.getId()))
                .sorted(getComparatorForType(params.getSortEnum()))
                .map(type -> mapper.toDTO(type,params.getLanguage()))
                .filter(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle()) < MAX_STRING_EDIT_DISTANCE)
                .sorted(Comparator.comparing(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle())))
                .sorted(getComparatorForTypeDTO(params.getSortEnum()))
                .collect(Collectors.toList());

        return listToPage(types, params.getPageNumber(), params.getPageSize());
    }

    private Page<TypeDTO> listToPage(List<TypeDTO> types, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), types.size());
        try{
            return new PageImpl<>(types.subList(start, end), pageable, types.size());
        }catch (IllegalArgumentException e){
            return new PageImpl<>(List.of(), pageable, types.size());
        }
    }

    private Comparator<Type> getComparatorForType(TypeSortEnum sortEnum){
        if (sortEnum == TypeSortEnum.POPULAR) {
            return (t1, t2) -> {
                long duration1 = t1.getActivities()
                        .stream()
                        .flatMap(activity -> activity.getAvailabilities().stream())
                        .filter(availability -> availability.getStartTime().isAfter(LocalDateTime.now()))
                        .mapToLong(availability -> Duration.between(availability.getStartTime(), availability.getEndTime()).abs().toMinutes())
                        .sum();
                long duration2 = t2.getActivities()
                        .stream()
                        .flatMap(activity -> activity.getAvailabilities().stream())
                        .filter(availability -> availability.getStartTime().isAfter(LocalDateTime.now()))
                        .mapToLong(availability -> Duration.between(availability.getStartTime(), availability.getEndTime()).abs().toMinutes())
                        .sum();
                return (int) (duration2 - duration1);
            };
        }
        return (t1, t2) -> 0;// Keep the previous order if no sort option provided

    }

    private Comparator<TypeDTO> getComparatorForTypeDTO(TypeSortEnum sortEnum){
        if (sortEnum == TypeSortEnum.TITLE) {
            return Comparator.comparing(TypeDTO::getTitle);
        }
        return (t1, t2) -> 0;// Keep the previous order if no sort option provided
    }
}
