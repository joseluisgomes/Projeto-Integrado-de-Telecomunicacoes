package com.example.demo.metereology;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/group2/api/weather")
@RequiredArgsConstructor
public class WeatherSampleController {
    private final WeatherSampleService weatherSampleService;

    @GetMapping("/samples")
    public List<WeatherSample> getAllSamples() {
        return weatherSampleService.findAllSamples();
    }
}

