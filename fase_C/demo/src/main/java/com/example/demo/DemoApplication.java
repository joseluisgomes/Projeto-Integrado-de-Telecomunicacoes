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
			try(final var serverSocket = new ServerSocket(PORT)) {
				while(true) {
					final var attemptSocket = serverSocket.accept();
					new Thread(new ServerWorker(repository, attemptSocket)).start();
				}
			} catch (IOException e) { e.printStackTrace(); }
		};
	}
}

class ServerWorker implements Runnable {
	private final WeatherSampleRepo repository;
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
			final var sample = buffer.readLine();

			if (PACKET_BELL) {
				try {
					if (PACKET_PER_GATEWAY.containsKey(gatewayID)) {
						outputStream.writeUTF(PACKET_PER_GATEWAY.get(gatewayID));
						outputStream.flush();
					}
				} catch (IOException e) { e.printStackTrace(); }
				PACKET_BELL = false;
			}

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