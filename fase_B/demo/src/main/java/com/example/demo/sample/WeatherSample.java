package com.example.demo.sample;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Weather")
@Table
@Getter
@Setter
public class WeatherSample {
    @Id
    @SequenceGenerator(
            name = "meteorology_id_generator",
            sequenceName = "meteorology_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "meteorology_id_generator"
    )
    private Long id;
    private final double temperature;
    private final double humidity;
    private final int pressure;
    private final LocalDateTime timeStamp;

    public WeatherSample() {
        this.temperature = 0.0;
        this.humidity = 0.0;
        this.pressure = 0;
        this.timeStamp = LocalDateTime.now();
    }

    public WeatherSample(double temperature,
                         double humidity,
                         int pressure,
                         LocalDateTime timeStamp) {
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
        return that.temperature == temperature &&
                    that.humidity == humidity &&
                        that.pressure == pressure &&
                            that.timeStamp.compareTo(timeStamp) == 0;
    }

    @Override
    public int hashCode() {
        /*
            31 equals to a shift and subtraction, for better performance
            on some computer architectures: 31 * i = (i << 5) - i
         */
        int result = timeStamp.hashCode();
        result = 31 * result + Integer.hashCode(pressure);
        result = 31 * result + Double.hashCode(humidity);
        result = 31 * result + Double.hashCode(temperature);
        return result;
    }

    @Override
    public String toString() {
        return "Meteorology{" +
                "id=" + id +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
