package com.example.active.data.entity.translation;

import lombok.Data;

import javax.persistence.*;

import java.io.Serializable;

@Embeddable
@Data
public class LanguageTranslationId implements Serializable {

    @Column(name = "TRANSLATION_ID")
    private Long translationId;

    @Column(name = "LANGUAGE_ID")
    private String languageId;
}
