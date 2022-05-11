package com.example.demo.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeatherSampleJob implements Runnable {
    private final Socket socket;
    private final WeatherSampleRepo repository;
    private final Logger logger = Logger.getAnonymousLogger();

    public WeatherSampleJob(Socket socket,
                            WeatherSampleRepo weatherSampleRepo) {
        this.socket = socket;
        this.repository = weatherSampleRepo;
    }

    @Override
    public void run() {
        try {
            final var inputStream = new InputStreamReader(socket.getInputStream());
            final var buffer = new BufferedReader(inputStream);
            final var sample = buffer.readLine();

            final var humidity = Double.parseDouble(sample.substring(0, 2));
            final var temperature = Double.parseDouble(sample.substring(2, 6));
            final var pressure = Integer.parseInt(sample.substring(6, 9));
            final var timeStamp = LocalDateTime.now();
            final var gatewayID = Long.parseLong(sample.substring(9,11));

            final var weatherSample = new WeatherSample(
                    gatewayID,
                    temperature,
                    humidity,
                    pressure,
                    timeStamp
            );
            this.repository.save(weatherSample);
            logger.log(Level.INFO,
                   "SAMPLE SAVED: " + weatherSample
            );

            socket.shutdownInput();
            inputStream.close();
            buffer.close();
            socket.close();
        } catch (IOException ioException) { ioException.printStackTrace(); }
    }
}
