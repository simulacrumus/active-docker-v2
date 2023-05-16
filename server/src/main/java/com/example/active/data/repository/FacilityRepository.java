package com.example.active.data.repository;

import com.example.active.data.entity.main.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findAllByCityId(Long cityId);
}
