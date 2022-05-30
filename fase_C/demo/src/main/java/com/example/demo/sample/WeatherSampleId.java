package com.example.demo.sample;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class WeatherSampleId implements Serializable {
    private Long gatewayID;
    private Long sampleID;

    public WeatherSampleId() {
        this.gatewayID = 0L;
        this.sampleID = 0L;
    }

    public WeatherSampleId(Long gatewayID, Long sampleID) {
        this.gatewayID = gatewayID;
        this.sampleID = sampleID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof WeatherSampleId sample))
            return false;
        return gatewayID.equals(sample.gatewayID) && sampleID.equals(sample.sampleID);
    }

    @Override
    public int hashCode() {
        return 31 * sampleID.hashCode() + gatewayID.hashCode();
    }

    @Override
    public String toString() {
        return "WeatherSampleId{" +
                "gatewayId=" + gatewayID +
                ", sampleId=" + sampleID +
                '}';
    }
}
