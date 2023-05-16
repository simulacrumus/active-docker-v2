package com.example.active.mapper;

import com.example.active.business.domain.FacilityDTO;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.data.entity.main.Facility;
import org.apache.lucene.util.SloppyMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FacilityMapper {
    @Autowired
    private AddressMapper addressMapper;

    public FacilityDTO toDTO(Facility facility, LanguageFilterEnum language, Double latitude, Double longitude){
        double distance = SloppyMath.haversinMeters(latitude, longitude, facility.getAddress().getLatitude(), facility.getAddress().getLongitude());
        return FacilityDTO.builder()
                .id(facility.getId())
                .email(facility.getEmail())
                .phone(facility.getPhone())
                .title(facility.getTitleTranslation().getTranslations().get(language.getValue()))
                .url(facility.getUrlTranslation().getTranslations().get(language.getValue()))
                .address(addressMapper.toDTO(facility.getAddress(),language))
                .latitude(facility.getAddress().getLatitude())
                .longitude(facility.getAddress().getLongitude())
                .distance(distance)
                .build();
    }
}
