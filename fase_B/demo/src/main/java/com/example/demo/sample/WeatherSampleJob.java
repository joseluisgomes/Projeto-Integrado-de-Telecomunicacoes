package com.example.demo.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class WeatherSampleJob implements Runnable {
    private final Socket socket;
    private final WeatherSampleRepo sampleRepo;

    public WeatherSampleJob(Socket socket,
                            WeatherSampleRepo sampleRepo) {
        this.socket = socket;
        this.sampleRepo = sampleRepo;
    }

    @Override
    public void run() {
        try {
            final var inputStream = new InputStreamReader(socket.getInputStream());
            final var buffer = new BufferedReader(inputStream);
            final List<String> sampleParameters = new ArrayList<>();
            String parameter;

            int i = 0;
            while ((parameter = buffer.readLine()) != null) {
                sampleParameters.add(parameter);
                if ((sampleParameters.size() % 4) == 0) {
                    final double temperature = Double.parseDouble(sampleParameters.get(i));
                    final double humidity = Double.parseDouble(sampleParameters.get(i + 1));
                    final int pressure = Integer.parseInt(sampleParameters.get(i + 2));
                    final int month = Integer.parseInt(sampleParameters.get(i + 3)); // TODO: CHECK THE TIMESTAMP FORMAT

                    final var sample = new WeatherSample(
                            temperature, humidity,
                            pressure, null
                    );
                    sampleRepo.save(sample);
                    i += 4;
                }
            }
            socket.shutdownInput();
            inputStream.close();
            buffer.close();
            socket.close();
        } catch (IOException ioException) { ioException.printStackTrace(); }
    }
}