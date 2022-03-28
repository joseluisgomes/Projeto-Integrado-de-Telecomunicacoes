package com.example.demo.metereology;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;

@Entity(name = "Meteorology")
@Table
@Getter @Setter
public class Meteorology {
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
    private final LocalDate timeStamp;

    public Meteorology() {
        this.temperature = 0.0;
        this.humidity = 0.0;
        this.pressure = 0;
        this.timeStamp = LocalDate.now();
    }

    public Meteorology(double temperature,
                       double humidity,
                       int pressure,
                       LocalDate timeStamp) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Meteorology that))
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
