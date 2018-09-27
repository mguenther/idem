package net.mguenther.idem.flake;

import net.mguenther.idem.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class IdGeneratorWorker<T> implements Runnable {

    private final IdGenerator<T> generator;

    private final List<T> generatedIds;

    private volatile boolean running = true;

    public IdGeneratorWorker(final IdGenerator<T> generator) {
        this.generator = generator;
        this.generatedIds = new ArrayList<>();
    }

    @Override
    public void run() {
        while (running) {
            generatedIds.add(generator.nextId());
        }
    }

    public void stop() {
        running = false;
    }

    public List<T> getGeneratedIds() {
        return generatedIds;
    }
}
