package com.example.active.business.service;

import com.example.active.business.domain.FacilityDTO;
import com.example.active.business.domain.TypeDTO;
import com.example.active.business.domain.paramobject.FacilityParams;
import com.example.active.business.domain.paramobject.TypeParams;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface FacilityService {

    FacilityDTO findById(FacilityParams params);

    Page<FacilityDTO> findAll(FacilityParams params);

    Page<FacilityDTO> findAllByType(FacilityParams params);
}
