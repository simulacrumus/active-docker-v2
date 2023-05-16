package com.example.active.data.entity.main;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "facility_branch")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacilityBranch {

    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FACILITY_ID")
    @ToString.Exclude
    private Facility facility;

    @OneToMany(mappedBy = "facilityBranch", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Availability> availabilities;

    @Column
    private String description;

    @Column(name = "RESERVATION_URL")
    private String reservationUrl;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Override
    public String toString() {
        return "FacilityBranch";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FacilityBranch that = (FacilityBranch) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
