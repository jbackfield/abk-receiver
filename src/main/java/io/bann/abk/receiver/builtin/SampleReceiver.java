package io.bann.abk.receiver.builtin;

import com.typesafe.config.Config;
import io.bann.abk.receiver.AbstractReceiver;

import java.util.Random;

public class SampleReceiver implements AbstractReceiver {

    private int minSize = 0;

    private int maxSize = 127;

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
    public void configure(Config config) {
        this.minSize = config.getInt("minSize");
        this.maxSize = config.getInt("maxSize");
    }

}
