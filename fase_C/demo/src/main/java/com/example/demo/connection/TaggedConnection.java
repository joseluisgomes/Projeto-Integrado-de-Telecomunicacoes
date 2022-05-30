package com.example.demo.connection;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaggedConnection implements AutoCloseable {
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final Lock readerLock;
    private final Lock writerLock;

    public TaggedConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new DataInputStream(
                new BufferedInputStream(socket.getInputStream())
        );
        this.outputStream = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream())
        );
        this.readerLock = new ReentrantLock();
        this.writerLock = new ReentrantLock();
    }

    public void send(long tag,
                     int connectionType,
                     List<byte[]> data) throws IOException {
        writerLock.lock();
        try {
            outputStream.writeLong(tag);
            outputStream.writeInt(connectionType);
            outputStream.writeInt(Objects.requireNonNull(data).size());
            for (byte[] array: data) {
                outputStream.writeInt(array.length);
                outputStream.write(array);
            }
            outputStream.flush();
        } finally { writerLock.unlock(); }
    }

    public Frame receive() throws IOException {
        readerLock.lock();
        try {
            final var tag = inputStream.readLong();
            final var typeConnection = inputStream.readInt();
            List<byte[]> list = null;
            if (inputStream.available() != 0) { // If there are any bytes to be read from the socket
                final var listLength = inputStream.readInt();
                list = new ArrayList<>(listLength);

                for (int i = 0; i < listLength; i++) {
                    int size = inputStream.readInt(); // Array's size
                    byte[] data = new byte[size];
                    inputStream.readFully(data);
                    list.add(data);
                }
            }
            return new Frame(tag, typeConnection, list);
        }  finally { readerLock.unlock(); }
    }

    @Override
    public void close() throws Exception { socket.close(); }

    public record Frame(Long tag, int typeConnection, List<byte[]> data) {} // <=> public static class Frame
}
