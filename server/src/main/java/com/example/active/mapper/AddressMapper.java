package com.example.active.mapper;

import com.example.active.business.domain.AddressDTO;
import com.example.active.business.domain.enums.LanguageFilterEnum;
import com.example.active.data.entity.main.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public AddressDTO toDTO(Address address, LanguageFilterEnum language){
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreetTranslation().getTranslations().get(language.getValue()))
                .city(address.getCity())
                .province(address.getProvince())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .build();
    }
}
