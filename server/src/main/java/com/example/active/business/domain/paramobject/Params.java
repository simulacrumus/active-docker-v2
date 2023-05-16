package com.example.active.business.domain.paramobject;

import com.example.active.business.domain.enums.CityFilterEnum;
import com.example.active.business.domain.enums.LanguageFilterEnum;

import java.util.Locale;

public abstract class Params {
    public Params() {
    }
    private LanguageFilterEnum language;
    private String city;

    public LanguageFilterEnum getLanguage() {
        return language;
    }

    public void setLanguage(LanguageFilterEnum language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
