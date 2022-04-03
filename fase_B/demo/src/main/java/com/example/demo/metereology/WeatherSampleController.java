package com.example.demo.metereology;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/group2/weather")
@RequiredArgsConstructor
public class WeatherSampleController {
    private final WeatherSampleService weatherSampleService;

    @GetMapping("/samples")
    public List<WeatherSample> getAllSamples() {
        return weatherSampleService.findAllSamples();
    }

    @PostMapping
    public void saveSample(@RequestBody WeatherSample sample) {
        weatherSampleService.saveSample(sample);
    }
}

