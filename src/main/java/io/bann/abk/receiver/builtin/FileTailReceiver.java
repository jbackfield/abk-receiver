package io.bann.abk.receiver.builtin;

import com.typesafe.config.Config;
import io.bann.abk.receiver.AbstractReceiver;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import java.io.File;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileTailReceiver implements AbstractReceiver, TailerListener {

    private String fileLocation;

    private File file;

    private Consumer<String> handler;

    private Tailer tailer;

    private Thread thread;

    private Logger logger;

    @Override
    public String getNextMessage() {
        return null;
    }

    public FileTailReceiver() {
        this.logger = Logger.getLogger(this.getClass().getCanonicalName());
    }

    @Override
    public void configure(Config config, Consumer<String> handler) {
        this.fileLocation = config.getString("fileLocation");
        this.file = new File(this.fileLocation);
        this.handler = handler;
        this.tailer = Tailer.builder().
                setFile(this.file).
                setTailerListener(this).
                setDelayDuration(Duration.ofMillis(config.getLong("delay"))).
                setTailFromEnd(false).
                setStartThread(false).
                setReOpen(true).
                get();
        this.thread = new Thread(this.tailer);
    }

    @Override
    public void start() {
        this.thread.start();
    }

    @Override
    public void end() {
        this.tailer.close();
    }

    @Override
    public void fileNotFound() {
        this.logger.severe(String.format("File Not Found %s", this.fileLocation));
    }

    @Override
    public void fileRotated() {
        this.logger.severe(String.format("File Not Found %s", this.fileLocation));
    }

    @Override
    public void handle(Exception e) {
        this.logger.log(Level.SEVERE, "Error", e);
    }

    @Override
    public void handle(String s) {
        this.handler.accept(s);
    }

    @Override
    public void init(Tailer tailer) {}
}
