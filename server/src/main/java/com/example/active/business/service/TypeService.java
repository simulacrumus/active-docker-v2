package com.example.active.business.service;

import com.example.active.business.domain.TypeDTO;
import com.example.active.business.domain.paramobject.TypeParams;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TypeService {
    TypeDTO getById(TypeParams params);

    Page<TypeDTO> findAll(TypeParams params);

    Page<TypeDTO> findAllByCategory(TypeParams params);

    Page<TypeDTO> findAllByFacility(TypeParams params);

    Page<TypeDTO> findAllByFacilityAndCategory(TypeParams params);
}
