package com.example.demo.metereology;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WeatherSampleService {
    private final WeatherSampleRepo weatherSampleRepo;

    public List<WeatherSample> findSampleByTemperature(double temperature) {
        return weatherSampleRepo.findSampleByTemperature(temperature)
                .orElseThrow(() -> new IllegalStateException(
                        "There is no samples with the specified temperature:" + temperature
                ));
    }


}
