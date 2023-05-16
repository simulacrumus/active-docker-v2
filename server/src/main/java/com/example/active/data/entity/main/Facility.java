package com.example.active.data.entity.main;

import com.example.active.data.entity.BaseEntity;
import javax.persistence.*;

import com.example.active.data.entity.translation.Translation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Table(name = "facility")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Facility extends BaseEntity {

    @Column
    private String phone;

    @Column
    private String email;

    @OneToOne
    @JoinColumn(name = "TITLE_TRANSLATION_ID")
    private Translation titleTranslation;

    @OneToOne
    @JoinColumn(name = "URL_TRANSLATION_ID")
    private Translation urlTranslation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CITY_ID")
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="ADDRESS_ID")
    private Address address;

    @OneToMany(mappedBy = "facility", fetch = FetchType.LAZY)
    private List<FacilityBranch> facilityBranches;

    @OneToMany(mappedBy = "facility")
    private Set<FacilityType> facilityTypes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Facility facility = (Facility) o;
        return phone.equals(facility.phone) && email.equals(facility.email) && titleTranslation.equals(facility.titleTranslation) && urlTranslation.equals(facility.urlTranslation) && address.equals(facility.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), phone, email, titleTranslation, urlTranslation, address);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Translation getTitleTranslation() {
        return titleTranslation;
    }

    public void setTitleTranslation(Translation titleTranslation) {
        this.titleTranslation = titleTranslation;
    }

    public Translation getUrlTranslation() {
        return urlTranslation;
    }

    public void setUrlTranslation(Translation urlTranslation) {
        this.urlTranslation = urlTranslation;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<FacilityBranch> getFacilityBranches() {
        return facilityBranches;
    }

    public void setFacilityBranches(List<FacilityBranch> facilityBranches) {
        this.facilityBranches = facilityBranches;
    }

    public Set<FacilityType> getFacilityTypes() {
        return facilityTypes;
    }

    public void setFacilityTypes(Set<FacilityType> facilityTypes) {
        this.facilityTypes = facilityTypes;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", titleTranslation=" + titleTranslation +
                ", urlTranslation=" + urlTranslation +
                ", address=" + address +
                '}';
    }
}
