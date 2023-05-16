package com.example.active.data.repository;

import com.example.active.data.entity.main.FacilityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityTypeRepository extends JpaRepository<FacilityType, Long> {
    List<FacilityType> findAllByFacilityId(Long facilityId);
    List<FacilityType> findAllByTypeId(Long typeId);
}
