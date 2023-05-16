package com.example.active.data.repository;

import com.example.active.data.entity.main.Category;
import com.example.active.data.entity.main.City;
import com.example.active.data.entity.main.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByCityId(Long cityId);
}
