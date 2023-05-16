package com.example.active.data.repository;

import com.example.active.data.entity.translation.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {

}
