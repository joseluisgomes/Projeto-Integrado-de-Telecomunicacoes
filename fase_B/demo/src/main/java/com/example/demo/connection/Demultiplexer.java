package com.example.demo.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demultiplexer implements AutoCloseable {
    private final TaggedConnection taggedConnection;
    private final Lock lock;
    private final Map<Long, FrameValue> frameMap;

    public Demultiplexer(TaggedConnection connection) {
        this.taggedConnection = connection;
        this.lock = new ReentrantLock();
        this.frameMap = new HashMap<>();
    }

    public void start() {
        new Thread(() -> {
            while(true) {

                try {
                    final var frame = taggedConnection.receive(); //receive data
                    lock.lock();
                    var frameValue = frameMap.get(frame.tag());
                    if (frameValue == null) { //check if there's a frame with that tag
                        frameValue = new FrameValue();
                        frameMap.put(frame.tag(), frameValue); //if not, adds new frame
                    }
                    frameValue.data = frame.data();
                    frameValue.condition.signalAll();
                    lock.unlock();
                } catch (IOException ioException) {
                    break;
                }
            }
        }).start();
    }

    public void send(Long tag,
                     int typeConnection,
                     List<byte[]> data) throws IOException {
        taggedConnection.send(tag, typeConnection, data);
    }

    public List<byte[]> receive(Long tag) throws InterruptedException {
        lock.lock();
        try {
            var frameValue = frameMap.get(tag);
            if (frameValue == null) {
                frameValue = new FrameValue();
                frameMap.put(tag, frameValue);
            }
            while(true) {
                if (!frameValue.data.isEmpty()) {
                    final var reply = frameValue.data;
                    frameMap.remove(tag); //removes from the map bc is not necessary anymore
                    return reply;
                }
                frameValue.condition.await(); //waits until there is something in the queue
            }
        } finally { lock.unlock(); }
    }

    @Override
    public void close() throws Exception { taggedConnection.close(); }

    private class FrameValue {
        List<byte[]> data;
        final Condition condition;

        public FrameValue() {
            this.data = new ArrayList<>();
            this.condition = lock.newCondition();
        }
    }
}
