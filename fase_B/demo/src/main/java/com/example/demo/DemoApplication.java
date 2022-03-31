package com.example.demo;

import com.example.demo.metereology.WeatherSample;
import com.example.demo.metereology.WeatherSampleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws IOException {
	/*	try(Socket socket = new Socket("localhost", 5000);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
		) {
			String sampleToSend;
			while(true) {
				while((sampleToSend = reader.readLine()) != null)
					System.out.println(sampleToSend);
				SpringApplication.run(DemoApplication.class, args);
			} */
			SpringApplication.run(DemoApplication.class, args);
		}

	@Bean
	CommandLineRunner commandLineRunner(WeatherSampleRepo weatherSampleRepo) {
		return args -> {
			weatherSampleRepo.deleteAll();

			final LocalDate timeStamp = LocalDate.of(2022, Month.MARCH, 28);
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
			weatherSampleRepo.findSampleByHumidity(89.0)
					.ifPresent(System.out::println);
		};
	}
}
