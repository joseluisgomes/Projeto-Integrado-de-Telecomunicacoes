package com.example.demo.metereology;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/group2/samples")
@RequiredArgsConstructor
public class WeatherSampleController {
    private final WeatherSampleService weatherSampleService;

    @GetMapping
    public List<WeatherSample> getAllSamples() {
        return weatherSampleService.findAllSamples();
    }

    @GetMapping(path = "{sampleId}")
    public WeatherSample getSampleById(@PathVariable(name = "sampleId") Long sampleId) {
        return weatherSampleService.findSampleById(sampleId);
    }

    @PostMapping
    public void saveSample(@RequestBody WeatherSample sample) {
        weatherSampleService.saveSample(sample);
    }

    @DeleteMapping(path = "{sampleId}")
    public void removeSampleById(@PathVariable(name = "sampleId") Long sampleId) {
        weatherSampleService.removeSampleById(sampleId);
    }
}

