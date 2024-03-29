package com.example.demo.sample;

import com.example.demo.DemoApplication;
import com.example.demo.ProtocolPacket;
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

    public WeatherSample findSampleById(Long gatewayID, Long sampleID) {
        log.info("Fetching sample with id {}", sampleID);
        return weatherSampleRepo.findById(new WeatherSampleId(gatewayID, sampleID))
                .orElseThrow(() -> new IllegalStateException("Sample with id " + sampleID + "not found."));
    }

    public void saveSample(WeatherSample sample) {
        log.info("Saving sample {} to the database", sample);
        weatherSampleRepo.save(sample);
    }

    public void removeSampleById(Long gatewayID, Long sampleID) {
        final var sample = new WeatherSampleId(gatewayID, sampleID);
        log.info("Removing sample with id {}", sample);
        if (!weatherSampleRepo.existsById(sample))
            throw new IllegalStateException("Sample with id" + sample + "not found.");
        else
            weatherSampleRepo.deleteById(sample);
    }

    public void saveProtocolMessage(ProtocolPacket packet) {
        final var gatewayID = Long.valueOf(packet.gatewayID());
        final var message = packet.message();

        log.info("Saving packet: {} to Gateway {}", packet, packet.gatewayID());
        DemoApplication.PACKET_PER_GATEWAY.put(gatewayID, message);
        DemoApplication.PACKET_BELL = true;
    }
}
