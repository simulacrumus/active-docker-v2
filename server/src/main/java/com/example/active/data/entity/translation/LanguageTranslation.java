package com.example.active.data.entity.translation;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "language_translation")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageTranslation {

    @EmbeddedId
    private LanguageTranslationId id;

    @Column
    private String description;

//    @ManyToOne
//    @MapsId("languageId")
//    @JoinColumn(name = "LANGUAGE_ID")
//    private Language language;
//
//    @ManyToOne
//    @MapsId("translationId")
//    @JoinColumn(name = "TRANSLATION_ID")
//    private Translation translation;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
