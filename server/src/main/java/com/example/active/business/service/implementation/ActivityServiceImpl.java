package com.example.active.business.service.implementation;

import com.example.active.business.domain.ActivityDTO;
import com.example.active.business.domain.TypeDTO;
import com.example.active.business.domain.enums.ActivitySortEnum;
import com.example.active.business.domain.enums.TypeSortEnum;
import com.example.active.business.domain.paramobject.ActivityParams;
import com.example.active.business.service.ActivityService;
import com.example.active.data.entity.main.*;
import com.example.active.data.repository.*;
import com.example.active.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.active.util.Utility.MAX_STRING_EDIT_DISTANCE;
import static com.example.active.util.Utility.stringDistanceScore;

@Service
@Qualifier("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FacilityTypeRepository facilityTypeRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private ActivityMapper mapper;

    private static final int DEFAULT_TIME_FILTER_DAY_RANGE = 7;

    @Override
    public ActivityDTO findById(ActivityParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        Availability availability = availabilityRepository.findById(params.getActivityId()).orElseThrow();
        if(!availability.getFacilityBranch().getFacility().getCity().getId().equals(city.getId())){
            throw new NoSuchElementException("Activity not found");
        }
        return mapper.toDTO(availability,params.getLanguage(), params.getLatitude(), params.getLongitude());
    }

    @Override
    public Page<ActivityDTO> findAll(ActivityParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        LocalDateTime startTime = getStartTime(params.getTime());
        LocalDateTime endTime = getEndTime(params.getTime());
        List<Availability> availabilities;
        // TODO: Is there a better way to handle empty boolean?
        if(params.getIsAvailable().isEmpty()){
            if(params.getTime().isPresent() && params.getTime().get().getHour() != 0){
                availabilities = availabilityRepository.findAllByStartTimeLessThanEqualAndEndTimeAfter(
                        startTime, endTime);
            } else {
                availabilities = availabilityRepository.findAllByStartTimeAfterAndEndTimeBefore(
                        startTime, endTime);
            }
        } else {
            if(params.getTime().isPresent() && params.getTime().get().getHour() != 0){
                availabilities = availabilityRepository.findAllByStartTimeLessThanEqualAndEndTimeAfterAndIsAvailable(
                       startTime, endTime, params.getIsAvailable().get());
            } else {
                availabilities = availabilityRepository.findAllByStartTimeAfterAndEndTimeBeforeAndIsAvailable(
                        startTime, endTime, params.getIsAvailable().get());
            }
        }
        if(params.getIsAvailable().isPresent() && params.getIsAvailable().get().equals(true)){
            availabilities = availabilities.stream().filter(availability -> availability.getEndTime().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        }
        List<ActivityDTO> activities = availabilities
                .stream()
                .filter(availability -> availability.getFacilityBranch().getFacility().getCity().getId().equals(city.getId()))
                .map(availability -> mapper.toDTO(availability, params.getLanguage(), params.getLatitude(), params.getLongitude()))
                .filter(activityDTO ->
                        Math.min(
                                stringDistanceScore(params.getQuery(), activityDTO.getTitle()),
                                Math.min(stringDistanceScore(params.getQuery(), activityDTO.getType()),
                                        stringDistanceScore(params.getQuery(), activityDTO.getFacility().getTitle()))
                        ) < MAX_STRING_EDIT_DISTANCE)
                .sorted(Comparator.comparing(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle())))
                .sorted(getComparator(params.getSortEnum()))
                .distinct()
                .collect(Collectors.toList());
        return listToPage(activities,params.getPageNumber(), params.getPageSize());
    }

    @Override
    public Page<ActivityDTO> findByType(ActivityParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        Type type = typeRepository.findById(params.getTypeId()).orElseThrow();
        if(!type.getCategory().getCity().getId().equals(city.getId())){
            throw new NoSuchElementException("Not found");
        }
        LocalDateTime startTime = getStartTime(params.getTime());
        LocalDateTime endTime = getEndTime(params.getTime());
        List<Availability> availabilities;
        // TODO: Is there a better way to handle empty boolean?
        if(params.getIsAvailable().isEmpty()){
            if(params.getTime().isPresent() && params.getTime().get().getHour() != 0){
                availabilities = availabilityRepository.findAllByActivityTypeIdAndStartTimeLessThanEqualAndEndTimeAfter(
                        params.getTypeId(), startTime, endTime);
            } else {
                availabilities = availabilityRepository.findAllByActivityTypeIdAndStartTimeAfterAndEndTimeBefore(
                        params.getTypeId(), startTime, endTime);
            }
        } else {
            if(params.getTime().isPresent() && params.getTime().get().getHour() != 0){
                availabilities = availabilityRepository.findAllByActivityTypeIdAndStartTimeLessThanEqualAndEndTimeAfterAndIsAvailable(
                        params.getTypeId(), startTime, endTime, params.getIsAvailable().get());
            } else {
                availabilities = availabilityRepository.findAllByActivityTypeIdAndStartTimeAfterAndEndTimeBeforeAndIsAvailable(
                        params.getTypeId(), startTime, endTime, params.getIsAvailable().get());
            }
        }
        if(params.getIsAvailable().isPresent() && params.getIsAvailable().get().equals(true)){
            availabilities = availabilities.stream().filter(availability -> availability.getEndTime().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        }
        List<ActivityDTO> activities = availabilities
                .stream()
                .map(availability -> mapper.toDTO(availability, params.getLanguage(), params.getLatitude(), params.getLongitude()))
                .filter(activityDTO ->
                        Math.min(
                                stringDistanceScore(params.getQuery(), activityDTO.getTitle()),
                                Math.min(stringDistanceScore(params.getQuery(), activityDTO.getType()),
                                        stringDistanceScore(params.getQuery(), activityDTO.getFacility().getTitle()))
                        ) < MAX_STRING_EDIT_DISTANCE)
                .distinct()
                .sorted(Comparator.comparing(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle())))
                .sorted(getComparator(params.getSortEnum()))
                .collect(Collectors.toList());
        return listToPage(activities,params.getPageNumber(), params.getPageSize());
    }

    @Override
    public Page<ActivityDTO> findByFacilityAndType(ActivityParams params) {
        City city = cityRepository.findByTitle(params.getCity()).orElseThrow();
        Type type = typeRepository.findById(params.getTypeId()).orElseThrow();
        Facility facility = facilityRepository.findById(params.getFacilityId()).orElseThrow();
        if(
                !type.getCategory().getCity().getId().equals(city.getId()) ||
                !facility.getCity().getId().equals(city.getId()) ||
                Collections.disjoint(type.getFacilityTypes(), facility.getFacilityTypes())
        ){
            throw new NoSuchElementException("Not found");
        }

        LocalDateTime startTime = getStartTime(params.getTime());
        LocalDateTime endTime = getEndTime(params.getTime());
        List<Availability> availabilities;
        // TODO: Is there a better way to handle empty boolean?
        if(params.getIsAvailable().isEmpty()){
            if(params.getTime().isPresent() && params.getTime().get().getHour() != 0){
                availabilities = availabilityRepository.findAllByFacilityBranchFacilityIdAndActivityTypeIdAndStartTimeLessThanEqualAndEndTimeAfter(
                        params.getFacilityId(), params.getTypeId(), startTime, endTime);
            } else {
                availabilities = availabilityRepository.findAllByFacilityBranchFacilityIdAndActivityTypeIdAndStartTimeAfterAndEndTimeBefore(
                        params.getFacilityId(), params.getTypeId(), startTime, endTime);
            }
        } else {
            if(params.getTime().isPresent() && params.getTime().get().getHour() != 0){
                availabilities = availabilityRepository.findAllByFacilityBranchFacilityIdAndActivityTypeIdAndStartTimeLessThanEqualAndEndTimeAfterAndIsAvailable(
                        params.getFacilityId(), params.getTypeId(), startTime, endTime, params.getIsAvailable().get());
            } else {
                availabilities = availabilityRepository.findAllByFacilityBranchFacilityIdAndActivityTypeIdAndStartTimeAfterAndEndTimeBeforeAndIsAvailable(
                        params.getFacilityId(), params.getTypeId(), startTime, endTime, params.getIsAvailable().get());
            }
        }
        if(params.getIsAvailable().isPresent() && params.getIsAvailable().get().equals(true)){
            availabilities = availabilities.stream().filter(availability -> availability.getEndTime().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        }
        List<ActivityDTO> activities = availabilities
                .stream()
                .map(availability -> mapper.toDTO(availability, params.getLanguage(), params.getLatitude(), params.getLongitude()))
                .filter(activityDTO -> Math.min(stringDistanceScore(params.getQuery(), activityDTO.getTitle()),
                        stringDistanceScore(params.getQuery(), activityDTO.getType())) < MAX_STRING_EDIT_DISTANCE)
                .distinct()
                .sorted(Comparator.comparing(typeDTO -> stringDistanceScore(params.getQuery(), typeDTO.getTitle())))
                .sorted(getComparator(params.getSortEnum()))
                .collect(Collectors.toList());
        return listToPage(activities,params.getPageNumber(), params.getPageSize());
    }

    private LocalDateTime getStartTime(Optional<LocalDateTime> time){
        return time.orElse(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
    }

    private LocalDateTime getEndTime(Optional<LocalDateTime> time){
        LocalDateTime startTime = getStartTime(time);
        LocalDate startDate = LocalDate.from(time.isEmpty() ? startTime.plusDays(DEFAULT_TIME_FILTER_DAY_RANGE) : startTime);
        return LocalDateTime.of(startDate, startTime.getHour() == 0 ? LocalTime.MAX : startTime.toLocalTime());
    }

    private Page<ActivityDTO> listToPage(List<ActivityDTO> activities, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), activities.size());
        try{
            return new PageImpl<>(activities.subList(start, end), pageable, activities.size());
        }catch (IllegalArgumentException e){
            return new PageImpl<>(List.of(), pageable, activities.size());
        }
    }

    private Comparator<ActivityDTO> getComparator(ActivitySortEnum sortEnum){
        switch (sortEnum){
            case DISTANCE:
                return Comparator.comparing((ActivityDTO activityDTO) -> activityDTO.getFacility().getDistance()).thenComparing(ActivityDTO::getStartTime);
            case TIME:
                return Comparator.comparing(ActivityDTO::getStartTime);
            default:
                return (t1, t2) -> 0;// Keep the previous order if there's no sort option provided
        }
    }
}
