package com.example.demo.server;

public enum Message { // Messages of the defined protocol
    START("START"), STOP("STOP"),
    RESTART("RESTART"), END("END");

    private final String message;

    Message(String message) { this.message = message; }

    public String getMessage() { return message; }
}
