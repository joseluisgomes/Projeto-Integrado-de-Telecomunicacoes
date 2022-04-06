package com.example.demo.metereology;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherSampleRepo extends JpaRepository<WeatherSample, Long> {
    // The inherited methods are enough for this project
}
