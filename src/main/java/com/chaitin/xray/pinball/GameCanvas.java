package com.chaitin.xray.pinball;

import java.awt.*;
import java.util.function.Consumer;

public class GameCanvas extends Canvas {
    private static final long serialVersionUID = 1L;
    private final Consumer<Graphics> consumer;
    public GameCanvas(Consumer<Graphics> consumer) {
        this.consumer = consumer;
    }
    @Override
    public void paint(Graphics g) {
        consumer.accept(g);
    }
}
