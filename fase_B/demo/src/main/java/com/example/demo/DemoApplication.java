package com.example.demo;

import com.example.demo.sample.WeatherSampleRepo;
import com.example.demo.server.ServerWrapper;
import io.socket.engineio.server.Emitter;
import io.socket.socketio.server.SocketIoSocket;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {
	private static final int PORT = 5000;
	private static final String ip = "192.168.1.7"; // IP address

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

		/*	weatherSampleRepo.deleteAll();

			final var timeStamp = LocalDateTime.now();
			final var sample1 = new WeatherSample(
					1L,
					20.0,
					89.0,
					300,
					timeStamp);
			final var sample2 = new WeatherSample(
					1L,
					24.5,
					89.0,
					300,
					timeStamp
			);
			weatherSampleRepo.saveAll(List.of(sample1, sample2));
			weatherSampleRepo.findAll().forEach(System.out::println); */
			final var serverWrapper = new ServerWrapper(ip, 8080, null);

			try { serverWrapper.startServer(); }
			catch (Exception e) { e.printStackTrace(); }

			final var server = serverWrapper.getSocketIoServer();
			final var nameSpace = server.namespace("/");
			nameSpace.on("START",
					objects -> {
						final var socket = (SocketIoSocket) objects[0];
						System.out.println(
								"Client " + socket.getId() +
										" (" + socket.getInitialHeaders().get("remote_address") + ") has connected."
						);
				});
		};
	}
}
