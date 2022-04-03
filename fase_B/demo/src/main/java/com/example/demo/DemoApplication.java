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
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {
	private static final List<WeatherSample> samples = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		try {
			final var clientSocket = new Socket("localhost", 5000);
			final var inputReader = new InputStreamReader(clientSocket.getInputStream());
			final var buffer = new BufferedReader(inputReader);
			final List<String> sampleParameters = new ArrayList<>();
			String parameter;

			int i = 0;
			while((parameter = buffer.readLine()) != null) {
				sampleParameters.add(parameter);
				if ((sampleParameters.size() % 4) == 0) {
					double temperature = Double.parseDouble(sampleParameters.get(i));
					double humidity = Double.parseDouble(sampleParameters.get(i + 1));
					int pressure = Integer.parseInt(sampleParameters.get(i + 2));
					int month = Integer.parseInt(sampleParameters.get(i + 3)); // TODO: CHECK THE TIMESTAMP FORMAT

					final var sample = new WeatherSample(
							temperature,
							humidity,
							pressure,
							null
					);
					samples.add(sample);
					i += 4;
				}
			}
			buffer.close();
				inputReader.close();
					clientSocket.shutdownInput();
						clientSocket.close();
		} catch (IOException ioException) { ioException.printStackTrace(); }
	}

	@Bean
	CommandLineRunner commandLineRunner(WeatherSampleRepo weatherSampleRepo) {
		return args -> {
			weatherSampleRepo.deleteAll();

			/* final var timeStamp = LocalDate.of(2022, Month.MARCH, 28);
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
					.ifPresent(System.out::println); */
			weatherSampleRepo.saveAll(samples);
		};
	}
}
