package com.example.demo.sample;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherSampleRepo extends JpaRepository<WeatherSample, WeatherSampleId> {
    // The inherited methods are enough for this project
}
