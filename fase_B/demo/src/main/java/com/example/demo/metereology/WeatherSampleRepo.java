package com.example.demo.metereology;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherSampleRepo extends JpaRepository<WeatherSample, Long> {

    @Query("SELECT s FROM Weather s WHERE s.temperature = ?1")
    Optional<List<WeatherSample>> findSampleByTemperature(double temperature);

    @Query("SELECT s FROM Weather s WHERE s.humidity = ?1")
    Optional<List<WeatherSample>> findSampleByHumidity(double humidity);

    @Query("SELECT s FROM Weather s WHERE s.pressure = ?1")
    Optional<List<WeatherSample>> findSampleByPressure(int pressure);
}
