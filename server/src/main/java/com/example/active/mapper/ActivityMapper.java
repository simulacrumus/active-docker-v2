package com.example.active.mapper;

import com.example.active.business.domain.ActivityDTO;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.data.entity.main.Availability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ActivityMapper {
    @Autowired
    private FacilityMapper facilityMapper;

    public ActivityDTO toDTO(Availability availability, LanguageFilterEnum language, Double latitude, Double longitude){
        return ActivityDTO.builder()
                .id(availability.getId())
                .title(availability.getActivity().getTitleTranslation().getTranslations().get(language.getValue()))
                .type(availability.getActivity().getType().getTitleTranslation().getTranslations().get(language.getValue()))
                .category(availability.getActivity().getType().getCategory().getTitleTranslation().getTranslations().get(language.getValue()))
                .facility(facilityMapper.toDTO(availability.getFacilityBranch().getFacility(), language, latitude, longitude))
                .isAvailable(availability.getIsAvailable())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .minAge(availability.getActivity().getMinAge())
                .maxAge(availability.getActivity().getMaxAge())
                .reservationURL(availability.getFacilityBranch().getReservationUrl())
                .lastUpdated(availability.getLastUpdated())
                .build();
    }

//    public Page<ActivityDTO> toDTOPage(Page<Availability> page, LanguageFilterEnum language, Double latitude, Double longitude){
//        return new PageImpl<>(
//                page.getContent().stream().map(availability -> toDTO(availability, language, latitude, longitude)).collect(Collectors.toList()),
//                page.getPageable(),
//                page.getTotalElements()
//        );
//    }
}
