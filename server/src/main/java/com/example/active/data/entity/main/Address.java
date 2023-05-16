package com.example.active.data.entity.main;

import com.example.active.data.entity.BaseEntity;
import javax.persistence.*;

import com.example.active.data.entity.translation.Translation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Table(name = "address")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntity {

    @Column
    private String city;

    @Column
    private String province;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column
    private String country;

    @Column
    private Double longitude;

    @Column
    private Double latitude;

    @OneToOne
    @JoinColumn(name = "STREET_TRANSLATION_ID")
    private Translation streetTranslation;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Translation getStreetTranslation() {
        return streetTranslation;
    }

    public void setStreetTranslation(Translation streetTranslation) {
        this.streetTranslation = streetTranslation;
    }
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String toString(String language) {
        return String.format("%s %s %s %s %s", getStreetTranslation().getTranslations().get(language), getCity(), getProvince(), getPostalCode(), getCountry());
    }
}
