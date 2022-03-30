package com.example.demo;

import com.example.demo.metereology.WeatherSample;
import com.example.demo.metereology.WeatherSampleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(WeatherSampleRepo weatherSampleRepo) {
		return args -> {
			weatherSampleRepo.deleteAll();

			LocalDate timeStamp = LocalDate.of(2022, Month.MARCH, 28);
			var sample1 = new WeatherSample(
					20.0,
					89.0,
					300,
					timeStamp);
			var sample2 = new WeatherSample(
					24.5,
					89.0,
					300,
					timeStamp
			);

			weatherSampleRepo.saveAll(List.of(sample1, sample2));
			weatherSampleRepo.findSampleByHumidity(89.0)
					.ifPresent(System.out::println);
		};
	}
}
