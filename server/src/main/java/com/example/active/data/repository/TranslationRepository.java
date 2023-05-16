package com.example.active.data.repository;

import com.example.active.data.entity.translation.LanguageTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRepository extends JpaRepository<LanguageTranslation, Long> {

}
