package com.projectwild.server;

public class UpdateLoop {

    private boolean running;
    private Thread updateThread;
    private Callback callback;

    private float tickrate;
    private double delta;

    public UpdateLoop() {
        tickrate = 10.0f;
        updateThread = new Thread(() -> {
            double ns = 1000000000.0 / tickrate;
            delta = 0;

            long lastTime = System.nanoTime();
            long timer = System.currentTimeMillis();

            while (running) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                while (delta >= 1) {
                    if(callback != null)
                        callback.callback();
                    delta--;
                }
            }
        });
    }

    public void setTickrate(int tickrate) {
        this.tickrate = (float) tickrate;
    }

    public double getDelta() {
        return delta;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void start() {
        running = true;
        updateThread.start();
    }

    public void stop() {
        running = false;
    }

    public interface Callback {
        void callback();
    }

}
