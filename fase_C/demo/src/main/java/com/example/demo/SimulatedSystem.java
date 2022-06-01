package com.example.demo;

import com.example.demo.sample.WeatherSample;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Random;

public class SimulatedSystem {

    public static void main(String[] args) {
        try(final var socket = new Socket("192.168.1.7", 5000)) { // The IP can change whenever You change your network
            final var outputStream = new ObjectOutputStream(socket.getOutputStream());
            final var random = new Random();

            while (true) {
                final var randomSample = new WeatherSample(
                        random.nextLong(),
                        random.nextDouble(),
                        random.nextDouble(),
                        random.nextInt(),
                        LocalDateTime.now()
                );
                outputStream.writeObject(randomSample);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
