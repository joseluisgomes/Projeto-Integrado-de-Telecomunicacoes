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
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.demo.DemoApplication.PACKET_PER_GATEWAY;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {
	private static final int PORT = 5000;
	public static boolean PACKET_BELL = false;
	public static Map<Long, String> PACKET_PER_GATEWAY = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(WeatherSampleRepo weatherSampleRepo) {
		return args -> {
			try(final var serverSocket = new ServerSocket(PORT)) {
				while(true) {
					final var attemptSocket = serverSocket.accept();
					new Thread(new ServerWorker(weatherSampleRepo, attemptSocket)).start();
				}
			}
		};
	}
}

class ServerWorker implements Runnable {
	private final DataOutputStream outputStream;
	private final WeatherSampleRepo repository;
	private final Socket socket;

	public ServerWorker(WeatherSampleRepo repository,
						Socket socket) throws IOException {
		this.repository = repository;
		this.outputStream = new DataOutputStream(
				new BufferedOutputStream(socket.getOutputStream())
		);
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			final var logger = Logger.getAnonymousLogger();
			final var inputStream = new InputStreamReader(socket.getInputStream());
			final var buffer = new BufferedReader(inputStream);
			var sample = buffer.readLine();
			final var gatewayID = Long.parseLong(sample.substring(9,11));

			new Thread(() -> {
				while (true) {
					if (DemoApplication.PACKET_BELL) {
						try {
							if (PACKET_PER_GATEWAY.containsKey(gatewayID)) {
								outputStream.writeUTF(PACKET_PER_GATEWAY.get(gatewayID));
								outputStream.flush();
							}
						} catch (IOException e) { e.printStackTrace(); }
						DemoApplication.PACKET_BELL = false;
					}
				}
			}).start();

			while (true) {
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
				this.repository.save(weatherSample);
				sample = buffer.readLine();

				logger.log(Level.INFO,
						"SAMPLE " + weatherSample + "SAVED");

				socket.shutdownInput();
				inputStream.close();
				buffer.close();
				socket.close();
			}
		} catch (IOException e) { e.printStackTrace(); }
	}
}