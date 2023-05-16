package com.example.active.business.service;

import com.example.active.business.domain.ActivityDTO;
import com.example.active.business.domain.paramobject.ActivityParams;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ActivityService{

    ActivityDTO findById(ActivityParams params);

    Page<ActivityDTO> findAll(ActivityParams params);

    Page<ActivityDTO> findByType(ActivityParams params);

    Page<ActivityDTO> findByFacilityAndType(ActivityParams params);

}