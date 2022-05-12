package com.example.demo;

import com.example.demo.sample.WeatherSampleJob;
import com.example.demo.sample.WeatherSampleRepo;
import com.example.demo.server.ServerWrapper;
import io.socket.socketio.server.SocketIoSocket;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;

import static com.example.demo.server.Message.*;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication {
	private static final int PORT = 5000;
	private static final String IP = "192.168.1.7"; // IP address

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(WeatherSampleRepo weatherSampleRepo) {
		return args -> {
			try(final var socket = new ServerSocket(PORT)) {
				final var serverWrapper = new ServerWrapper(IP, 8080, null);
				serverWrapper.startServer();

				final var server = serverWrapper.getSocketIoServer();
				while(true) {
					final var nameSpace = server.namespace("/");
					final var attemptSocket = socket.accept();
					new Thread(new WeatherSampleJob(attemptSocket, weatherSampleRepo))
							.start();

					final var writer = new PrintWriter(
							new BufferedOutputStream(attemptSocket.getOutputStream())
					);

					nameSpace.on(START.name(), objects -> {
						final var ioSocket = (SocketIoSocket) objects[0];
						System.out.println(
								"Client " + ioSocket.getId() +
										" (" + ioSocket.getInitialHeaders().get("remote_address") + ") has connected."
						);
						writer.print(START.name());
						writer.flush();
					});
					nameSpace.on(STOP.name(), objects -> {
						final var ioSocket = (SocketIoSocket) objects[0];
						System.out.println(
								"Client " + ioSocket.getId() +
										" (" + ioSocket.getInitialHeaders().get("remote_address") + ") has connected."
						);
						writer.print(STOP.name());
						writer.flush();
					});
					nameSpace.on(RESTART.name(), objects -> {
						final var ioSocket = (SocketIoSocket) objects[0];
						System.out.println(
								"Client " + ioSocket.getId() +
										" (" + ioSocket.getInitialHeaders().get("remote_address") + ") has connected."
						);
						writer.print(RESTART.name());
						writer.flush();
					});
					nameSpace.on(END.name(), objects -> {
						final var ioSocket = (SocketIoSocket) objects[0];
						System.out.println(
								"Client " + ioSocket.getId() +
										" (" + ioSocket.getInitialHeaders().get("remote_address") + ") has connected."
						);
						writer.print(END.name());
						writer.flush();
					});
				}
			} catch (Exception e) { e.printStackTrace(); }
		};
	}
}
