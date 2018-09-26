package net.mguenther.idem.flake;

import java.util.ArrayList;
import java.util.List;

public class Flake64Worker<T> implements Runnable {

    private final Flake64<T> flake;

    private final List<T> generatedIds;

    private volatile boolean running = true;

    public Flake64Worker(final Flake64<T> flake) {
        this.flake = flake;
        this.generatedIds = new ArrayList<>();
    }

    @Override
    public void run() {
        while (running) {
            generatedIds.add(flake.nextId());
        }
    }

    public void stop() {
        running = false;
    }

    public List<T> getGeneratedIds() {
        return generatedIds;
    }
}
