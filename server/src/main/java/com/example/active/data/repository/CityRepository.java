package com.example.active.data.repository;

import com.example.active.data.entity.main.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByTitle(String title);
}
