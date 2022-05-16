package com.example.demo;

import com.example.demo.sample.WeatherSample;
import com.example.demo.sample.WeatherSampleJob;
import com.example.demo.sample.WeatherSampleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.ServerSocket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {
	private static final int PORT = 5000;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(WeatherSampleRepo weatherSampleRepo) {
		return args -> {
		/*	try(final var socket = new ServerSocket(PORT)) {
				while(true) {
					final var attemptSocket = socket.accept();
					new Thread(new WeatherSampleJob(attemptSocket, weatherSampleRepo))
							.start();
				}
			} catch (Exception e) { e.printStackTrace(); } */

			weatherSampleRepo.deleteAll();

			final var timeStamp = LocalDateTime.now();
			final var sample1 = new WeatherSample(
					1L,
					20.0,
					89.0,
					300,
					timeStamp
			);
			final var sample2 = new WeatherSample(
					1L,
					24.5,
					89.0,
					300,
					timeStamp
			);
			weatherSampleRepo.saveAll(List.of(sample1, sample2));
			weatherSampleRepo.findAll().forEach(System.out::println);
		};
	}
}