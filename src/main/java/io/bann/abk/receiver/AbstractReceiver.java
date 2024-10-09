package io.bann.abk.receiver;

import com.typesafe.config.Config;

import java.util.function.Consumer;

public interface AbstractReceiver {

    String getNextMessage();

    void configure(Config config, Consumer<String> handler);

    void start();

    void end();

}
