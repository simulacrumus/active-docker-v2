package com.example.active.data.repository;

import com.example.active.data.entity.main.FacilityBranch;
import com.example.active.data.entity.main.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityBranchRepository extends JpaRepository<FacilityBranch, Long> {

}
