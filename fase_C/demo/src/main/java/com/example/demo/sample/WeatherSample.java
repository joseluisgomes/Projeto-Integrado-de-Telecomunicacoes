package com.example.demo.sample;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "Weather")
@Table(name = "weather")
@IdClass(value = WeatherSampleId.class)
@Getter
@Setter
public class WeatherSample implements Serializable {
    @Id
    private Long gatewayID;
    @Id
    private Long sampleID;
    private final double temperature;
    private final double humidity;
    private final int pressure;
    private final LocalDateTime timeStamp;
    private static long counter = 1; // Sample counter

    public WeatherSample() {
        this.temperature = 0.0;
        this.humidity = 0.0;
        this.pressure = 0;
        this.timeStamp = LocalDateTime.now();
    }

    public WeatherSample(Long gatewayID,
                         double temperature,
                         double humidity,
                         int pressure,
                         LocalDateTime timeStamp) {
        this.gatewayID = gatewayID;
        this.sampleID = WeatherSample.counter++;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof WeatherSample that))
            return false;
        return gatewayID.equals(that.gatewayID) && sampleID.equals(that.sampleID);
    }

    @Override
    public int hashCode() {
        /*
            31 equals to a shift and subtraction, for better performance
            on some computer architectures: 31 * i = (i << 5) - i
         */
        return 31 * sampleID.hashCode() + gatewayID.hashCode();
    }

    @Override
    public String toString() {
        return "WeatherSample{" +
                "gatewayID=" + gatewayID +
                ", sampleID=" + sampleID +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
