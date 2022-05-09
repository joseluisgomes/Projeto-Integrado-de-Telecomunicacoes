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
        if (!(o instanceof WeatherSampleId that))
            return false;
        return gatewayID.equals(that.gatewayID) && sampleID.equals(that.sampleID);
    }

    @Override
    public int hashCode() {
        int result = sampleID.hashCode();
        return 31 * result + gatewayID.hashCode();
    }

    @Override
    public String toString() {
        return "WeatherSampleId{" +
                "gatewayId=" + gatewayID +
                ", sampleId=" + sampleID +
                '}';
    }
}
