package com.example.demo.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/group2/samples")
@RequiredArgsConstructor
public class WeatherSampleController {
    private final WeatherSampleService weatherSampleService;

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public List<WeatherSample> getAllSamples() {
        return weatherSampleService.findAllSamples();
    }

/*    @GetMapping(path = "{sampleId}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
    public WeatherSample getSampleById(@PathVariable(name = "sampleId") Long sampleId) {
        return weatherSampleService.findSampleById(sampleId);
    } */

    @PostMapping
    @PreAuthorize(value = "hasAuthority('sample:write')")
    public void saveSample(@RequestBody WeatherSample sample) {
        weatherSampleService.saveSample(sample);
    }

  /*  @DeleteMapping(path = "{sampleId}")
    @PreAuthorize(value = "hasAuthority('sample:write')")
    public void removeSampleById(@PathVariable(name = "sampleId") Long sampleId) {
        weatherSampleService.removeSampleById(sampleId);
    } */
}

