package com.example.demo.sample;

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

 /*   public WeatherSample findSampleById(Long sampleId) {
        log.info("Fetching sample with id {}", sampleId);
        return weatherSampleRepo.findById(sampleId)
                .orElseThrow(() -> new IllegalStateException(
                        "Sample with id " + sampleId + "not found."));
    } */

    public void saveSample(WeatherSample sample) {
        log.info("Saving sample {} to the database", sample);
        weatherSampleRepo.save(sample);
    }

 /*   public void removeSampleById(Long sampleId) {
        log.info("Removing sample with id {}", sampleId);
        boolean isPresent = weatherSampleRepo.existsById(sampleId);
        if (isPresent)
            weatherSampleRepo.deleteById(sampleId);
        else
            throw new IllegalStateException(
                    "Sample with id " + sampleId + "not found."
            );
    } */
}
