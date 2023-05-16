package com.example.active.business.service;

import com.example.active.business.domain.CategoryDTO;
import com.example.active.business.domain.TypeDTO;
import com.example.active.business.domain.paramobject.CategoryParams;
import com.example.active.business.domain.paramobject.TypeParams;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CategoryService {
    CategoryDTO findById(CategoryParams params);

    Page<CategoryDTO> findAll(CategoryParams params);

    Page<CategoryDTO> findAllByFacility(CategoryParams params);
}
