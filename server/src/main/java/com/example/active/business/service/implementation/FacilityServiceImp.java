package com.example.active.business.service.implementation;

import com.example.active.business.domain.FacilityDTO;
import com.example.active.business.domain.enums.FacilitySortEnum;
import com.example.active.business.domain.paramobject.FacilityParams;
import com.example.active.business.service.FacilityService;
import com.example.active.data.entity.main.City;
import com.example.active.data.entity.main.Facility;
import com.example.active.data.entity.main.Type;
import com.example.active.data.repository.CityRepository;
import com.example.active.data.repository.FacilityRepository;
import com.example.active.data.repository.FacilityTypeRepository;
import com.example.active.data.repository.TypeRepository;
import com.example.active.mapper.FacilityMapper;
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
@Qualifier("facilityService")
public class FacilityServiceImp implements FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private FacilityTypeRepository facilityTypeRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private FacilityMapper mapper;

    @Override
    public FacilityDTO findById(FacilityParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        Facility facility = facilityRepository.findById(params.getFacilityId()).orElseThrow();
        if(!facility.getCity().getId().equals(city.getId())){
            throw new NoSuchElementException("Facility not found");
        }
        return mapper.toDTO(facility, params.getLanguage(), params.getLatitude(), params.getLongitude());
    }

    @Override
    public Page<FacilityDTO> findAll(FacilityParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        List<FacilityDTO> facilities = city.getFacilities()
                .stream()
                .map(facility -> mapper.toDTO(facility, params.getLanguage(), params.getLatitude(), params.getLongitude()))
                .filter(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle()) < MAX_STRING_EDIT_DISTANCE)
                .sorted(Comparator.comparing(facilityDTO -> stringDistanceScore(params.getQuery(), facilityDTO.getTitle())))
                .sorted(getComparator(params.getSortEnum()))
                .collect(Collectors.toList());
        return listToPage(facilities, params.getPageNumber(), params.getPageSize());
    }

    @Override
    public Page<FacilityDTO> findAllByType(FacilityParams params) {
        Type type = typeRepository.findById(params.getTypeId()).orElseThrow();
        if(!type.getCategory().getCity().getTitle().equalsIgnoreCase(params.getCity())){
            throw new NoSuchElementException("Type not found");
        }
        List<FacilityDTO> facilities = type.getFacilityTypes()
                .stream()
                .map(facilityType -> mapper.toDTO(facilityType.getFacility(), params.getLanguage(), params.getLatitude(), params.getLongitude()))
                .filter(facilityDTO -> stringDistanceScore(params.getQuery(), facilityDTO.getTitle()) < MAX_STRING_EDIT_DISTANCE)
                .distinct()
                .sorted(Comparator.comparing(facilityDTO -> stringDistanceScore(params.getQuery(), facilityDTO.getTitle())))
                .sorted(getComparator(params.getSortEnum()))
                .collect(Collectors.toList());
        return listToPage(facilities, params.getPageNumber(), params.getPageSize());
    }

    private Page<FacilityDTO> listToPage(List<FacilityDTO> facilities, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), facilities.size());
        try{
            return new PageImpl<>(facilities.subList(start, end), pageable, facilities.size());
        }catch (IllegalArgumentException e){
            return new PageImpl<>(List.of(), pageable, facilities.size());
        }
    }

    private Comparator<FacilityDTO> getComparator(FacilitySortEnum sortEnum){
        switch (sortEnum) {
            case DISTANCE:
                return Comparator.comparingDouble(FacilityDTO::getDistance);
            case TITLE:
                return Comparator.comparing(FacilityDTO::getTitle);
            default:
                return (f1,f2) -> 0;// Keep the previous order if there's no sort option provided
        }
    }
}
