package com.example.demo.sample;

import com.example.demo.ProtocolPacket;
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

    @GetMapping(path = {"{gatewayID}", "{sampleID}"})
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
    public WeatherSample getSampleById(@PathVariable(name = "gatewayID") Long gatewayID,
                                       @PathVariable(name = "sampleId") Long sampleID) {
        return weatherSampleService.findSampleById(gatewayID, sampleID);
    }

    @PostMapping
    @PreAuthorize(value = "hasAuthority('sample:write')")
    public void saveSample(@RequestBody WeatherSample sample) {
        weatherSampleService.saveSample(sample);
    }

    @DeleteMapping(path = {"{gatewayID}", "{sampleID}"})
    @PreAuthorize(value = "hasAuthority('sample:write')")
    public void removeSampleById(@PathVariable(name = "gatewayID") Long gatewayID,
                                 @PathVariable(name = "sampleId") Long sampleID) {
        weatherSampleService.removeSampleById(gatewayID, sampleID);
    }

    @PostMapping(path = "/protocol")
    public void saveProtocolMessage(@RequestBody ProtocolPacket packet) {
        weatherSampleService.saveProtocolMessage(packet);
    }
}

