package com.example.active.data.repository;

import com.example.active.data.entity.main.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {
    List<Type> findAllByCategoryCityId(Long cityId);
}