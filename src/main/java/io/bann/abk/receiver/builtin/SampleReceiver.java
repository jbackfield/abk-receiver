package io.bann.abk.receiver.builtin;

import com.typesafe.config.Config;
import io.bann.abk.receiver.AbstractReceiver;

import java.util.Random;
import java.util.function.Consumer;

import java.util.logging.Logger;

public class SampleReceiver implements AbstractReceiver {

    private Logger logger;

    private int minSize = 0;

    private int maxSize = 127;

    private long delay = 1;

    private Thread thread;

    private Consumer<String> handler;

    private Boolean running;

    @Override
    public String getNextMessage() {
        String message = new Random().
                ints(97, 123).
                limit(new Random().nextInt(this.minSize, this.maxSize)).
                collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).
                toString();

        return message;
    }

    @Override
    public void configure(Config config, Consumer<String> handler) {
        this.minSize = config.getInt("minSize");
        this.maxSize = config.getInt("maxSize");
        this.delay = config.getLong("delay");
        this.handler = handler;
        this.thread = new Thread(this::execute);
        this.running = false;
    }

    public void execute() {
        while(this.running) {
            this.handler.accept(this.getNextMessage());
            try {
                Thread.sleep(this.delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        this.running = true;
        this.thread.start();
    }

    @Override
    public void end() {
        this.running = false;
    }

}
