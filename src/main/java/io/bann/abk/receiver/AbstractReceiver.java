package io.bann.abk.receiver;

import com.typesafe.config.Config;

public interface AbstractReceiver {

    String getNextMessage();

    void configure(Config config);

}
