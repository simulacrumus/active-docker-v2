package com.example.active.data.entity.main;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityTypeId implements Serializable {

    @Column(name = "FACILITY_ID")
    private Long facilityId;

    @Column(name = "TYPE_ID")
    private Long typeId;

}