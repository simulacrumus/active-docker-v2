package com.example.active.data.repository;

import com.example.active.data.entity.main.Availability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    List<Availability> findAllByStartTimeAfterAndEndTimeBefore(
            LocalDateTime start,
            LocalDateTime end
    );

    List<Availability> findAllByStartTimeAfterAndEndTimeBeforeAndIsAvailable(
            LocalDateTime start,
            LocalDateTime end,
            Boolean isAvailable
    );

    List<Availability> findAllByActivityTypeIdAndStartTimeAfterAndEndTimeBefore(
            Long typeId,
            LocalDateTime start,
            LocalDateTime end
    );
    List<Availability> findAllByActivityTypeIdAndStartTimeAfterAndEndTimeBeforeAndIsAvailable(
            Long typeId,
            LocalDateTime start,
            LocalDateTime end,
            Boolean isAvailable
    );

    List<Availability> findAllByFacilityBranchFacilityIdAndActivityTypeIdAndStartTimeAfterAndEndTimeBefore(
            Long facilityId,
            Long typeId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Availability> findAllByFacilityBranchFacilityIdAndActivityTypeIdAndStartTimeAfterAndEndTimeBeforeAndIsAvailable(
            Long facilityId,
            Long typeId,
            LocalDateTime start,
            LocalDateTime end,
            Boolean isAvailable
    );

    List<Availability> findAllByStartTimeLessThanEqualAndEndTimeAfter(
            LocalDateTime start,
            LocalDateTime end
    );

    List<Availability> findAllByStartTimeLessThanEqualAndEndTimeAfterAndIsAvailable(
            LocalDateTime start,
            LocalDateTime end,
            Boolean isAvailable
    );

    List<Availability> findAllByActivityTypeIdAndStartTimeLessThanEqualAndEndTimeAfter(
            Long typeId,
            LocalDateTime start,
            LocalDateTime end
    );
    List<Availability> findAllByActivityTypeIdAndStartTimeLessThanEqualAndEndTimeAfterAndIsAvailable(
            Long typeId,
            LocalDateTime start,
            LocalDateTime end,
            Boolean isAvailable
    );

    List<Availability> findAllByFacilityBranchFacilityIdAndActivityTypeIdAndStartTimeLessThanEqualAndEndTimeAfter(
            Long facilityId,
            Long typeId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Availability> findAllByFacilityBranchFacilityIdAndActivityTypeIdAndStartTimeLessThanEqualAndEndTimeAfterAndIsAvailable(
            Long facilityId,
            Long typeId,
            LocalDateTime start,
            LocalDateTime end,
            Boolean isAvailable
    );
}
