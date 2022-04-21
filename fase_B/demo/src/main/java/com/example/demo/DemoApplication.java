package com.example.demo;

import com.example.demo.sample.WeatherSample;
import com.example.demo.sample.WeatherSampleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
@SpringBootApplication
public class DemoApplication {
	private static final List<WeatherSample> samples = new ArrayList<>();
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(WeatherSampleRepo weatherSampleRepo) {
		return args -> {
		/*	try {
				final var serverSocket = new ServerSocket(5000);
				while(true) {
					final var attemptSocket = serverSocket.accept();
					final var thread = new Thread(new WeatherSampleJob(
							attemptSocket, weatherSampleRepo
					));
					thread.start();
				}
			} catch (IOException ioException) {
				ioException.printStackTrace();
			} */
			weatherSampleRepo.deleteAll();

			// Simulated systems
			final var timeStamp = LocalDate.of(2022, Month.MARCH, 28);
			final var sample1 = new WeatherSample(
					20.0,
					89.0,
					300,
					timeStamp
			);
			final var sample2 = new WeatherSample(
					24.5,
					89.0,
					300,
					timeStamp
			);
			final var sample3 = new WeatherSample(
					28.8,
					76.0,
					276,
					timeStamp
			);
			weatherSampleRepo.saveAll(List.of(sample1, sample2, sample3));
			weatherSampleRepo.findAll().forEach(System.out::println);
			//weatherSampleRepo.saveAll(samples);
		};
	}
}
