package com.example.active.data.entity.main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "facility_type")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FacilityType implements Serializable {

    @EmbeddedId
    private FacilityTypeId facilityTypeId;

    @JsonIgnore
    @Column(name = "LAST_UPDATED")
    private LocalDateTime lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FACILITY_ID")
    @MapsId("facilityId")
    @ToString.Exclude
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="TYPE_ID")
    @MapsId("typeId")
    @ToString.Exclude
    private Type type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FacilityType that = (FacilityType) o;
        return facilityTypeId != null && Objects.equals(facilityTypeId, that.facilityTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facilityTypeId);
    }
}
