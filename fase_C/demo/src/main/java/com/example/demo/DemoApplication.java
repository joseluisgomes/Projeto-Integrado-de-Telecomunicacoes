package com.example.demo;

import com.example.demo.sample.WeatherSample;
import com.example.demo.sample.WeatherSampleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.DemoApplication.*;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {
	private static final int PORT = 5000;
	public static boolean PACKET_BELL = false;
	public static long gatewayID = 1L;
	public static Map<Long, String> PACKET_PER_GATEWAY = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(WeatherSampleRepo repository) {
		return args -> {
		/*	try(final var serverSocket = new ServerSocket(PORT)) {
				while(true) {
					final var attemptSocket = serverSocket.accept();
					new Thread(new ServerWorker(repository, attemptSocket)).start();
				}
			} catch (IOException e) { e.printStackTrace(); } */

			repository.deleteAll();

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
			final var sample3 = new WeatherSample(
					2L,
					25.2,
					68.0,
					650,
					timeStamp
			);
			final var sample4 = new WeatherSample(
					2L,
					24.8,
					65.6,
					562,
					timeStamp
			);
			final var sample5 = new WeatherSample(
					3L,
					24.2,
					74.3,
					651,
					timeStamp
			);
			final var sample6 = new WeatherSample(
					3L,
					25.7,
					76.8,
					587,
					timeStamp
			);
			repository.saveAll(List.of(sample1, sample2, sample3, sample4, sample5, sample6));
			repository.findAll().forEach(System.out::println);
		};
	}
}

class ServerWorker implements Runnable {
	private final WeatherSampleRepo repository; // TODO: application.properties -> alterar create-drop para update
	private final Socket socket;

	public ServerWorker(WeatherSampleRepo repository,
						Socket socket) {
		this.repository = repository;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			final var inputStream = new InputStreamReader(socket.getInputStream());
			final var outputStream = new DataOutputStream(
					new BufferedOutputStream(socket.getOutputStream())
			);
			final var buffer = new BufferedReader(inputStream);

			try {
				int we=0;
				if(PACKET_BELL) {
					if (PACKET_PER_GATEWAY.containsKey(gatewayID)) {
						switch (PACKET_PER_GATEWAY.get(gatewayID)) {
							case "START" -> {
								outputStream.write('1');
								outputStream.flush();
							}
							case "STOP" -> {
								outputStream.write('2');
								outputStream.flush();
							}
							case "PAUSE" -> {
								outputStream.write('3');
								outputStream.flush();
								PACKET_BELL = false;
								while (!PACKET_BELL) ;
								outputStream.write('4');
								outputStream.flush();
							}
							case "RESTART" -> {
								outputStream.write('4');
								outputStream.flush();
							}
						}
					}
				}
				outputStream.write(we);
				outputStream.flush();
			} catch (IOException e) { e.printStackTrace(); }

			final var sample = buffer.readLine();

			PACKET_BELL = false;

			final var humidity = Double.parseDouble(sample.substring(0, 2));
			final var temperature = Double.parseDouble(sample.substring(2, 6));
			final var pressure = Integer.parseInt(sample.substring(6, 9));
			final var timeStamp = LocalDateTime.now();
			final var weatherSample = new WeatherSample(
					gatewayID,
					temperature,
					humidity,
					pressure,
					timeStamp
			);

			repository.save(weatherSample);
			socket.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
}