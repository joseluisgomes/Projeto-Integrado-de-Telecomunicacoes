package com.example.demo.metereology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class WeatherSampleJob implements Runnable {
    private final Socket socket;

    public WeatherSampleJob(Socket socket) {
        this.socket = socket;
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
                    double temperature = Double.parseDouble(sampleParameters.get(i));
                    double humidity = Double.parseDouble(sampleParameters.get(i + 1));
                    int pressure = Integer.parseInt(sampleParameters.get(i + 2));
                    int month = Integer.parseInt(sampleParameters.get(i + 3)); //// TODO: CHECK THE TIMESTAMP FORMAT

                    final var sample = new WeatherSample(
                            temperature, humidity,
                            pressure, null
                    );
                    i += 4;
                }
            }
            buffer.close();
                inputStream.close();
                    socket.shutdownInput();
                        socket.close();
        } catch (IOException ioException) { ioException.printStackTrace(); }
    }
}
