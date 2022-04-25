package com.example.demo;

import com.example.demo.sample.WeatherSampleJob;
import com.example.demo.sample.WeatherSampleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.ServerSocket;

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
			try(final var socket = new ServerSocket(PORT)) {
				while(true) {
					final var attemptSocket = socket.accept();
					new Thread(new WeatherSampleJob(attemptSocket, weatherSampleRepo))
							.start();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		};
	}
}
