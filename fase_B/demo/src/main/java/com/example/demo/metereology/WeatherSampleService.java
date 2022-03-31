package com.example.demo.metereology;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class WeatherSampleService {
    private final WeatherSampleRepo weatherSampleRepo;

    public List<WeatherSample> findAllSamples() {
        log.info("Fetching all samples");
        return weatherSampleRepo.findAll();
    }

    public void saveSample(WeatherSample sample) {
        log.info("Saving sample {} to the database", sample);
        weatherSampleRepo.save(sample);
    }

    public List<WeatherSample> findSampleByTemperature(double temperature) {
        log.info("Fetching all samples with temperature: {}", temperature);
        return weatherSampleRepo.findSampleByTemperature(temperature)
                .orElseThrow(() -> new IllegalStateException(
                        "There are no samples with the specified temperature:" + temperature
                ));
    }

    public List<WeatherSample> findSampleByHumidity(double humidity) {
        log.info("Fetching all samples with humidity: {}", humidity);
        return weatherSampleRepo.findSampleByHumidity(humidity)
                .orElseThrow(() -> new IllegalStateException(
                        "There are no samples with the specified humidity:" + humidity
                ));
    }

    public List<WeatherSample> findSampleByPressure(int pressure) {
        log.info("Fetching all samples with pressure: {}", pressure);
        return weatherSampleRepo.findSampleByPressure(pressure)
                .orElseThrow(() -> new IllegalStateException(
                        "There are no samples with the specified pressure:" + pressure
                ));
    }
}
