package com.example.demo;

import com.example.demo.metereology.WeatherSample;
import com.example.demo.metereology.WeatherSampleJob;
import com.example.demo.metereology.WeatherSampleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {
	private static final List<WeatherSample> samples = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		int i = 0;
        try {
            final var serverSocket = new ServerSocket(5000);
            while(true) {
                final var attemptSocket = serverSocket.accept();
				final var thread = new Thread(new WeatherSampleJob(attemptSocket));
				thread.setName("Thread " + i);
				thread.start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
	}

	@Bean
	CommandLineRunner commandLineRunner(WeatherSampleRepo weatherSampleRepo) {
		return args -> {
			weatherSampleRepo.deleteAll();

			final var timeStamp = LocalDate.of(2022, Month.MARCH, 28);
			final var sample1 = new WeatherSample(
					20.0,
					89.0,
					300,
					timeStamp);
			final var sample2 = new WeatherSample(
					24.5,
					89.0,
					300,
					timeStamp
			);
			weatherSampleRepo.saveAll(List.of(sample1, sample2));
			weatherSampleRepo.findAll().forEach(System.out::println);
			//weatherSampleRepo.saveAll(samples);
		};
	}
}
