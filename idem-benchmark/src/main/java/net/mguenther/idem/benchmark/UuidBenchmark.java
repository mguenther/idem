package net.mguenther.idem.benchmark;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@State
public class UuidBenchmark {

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void generateId() {
        UUID.randomUUID().toString();
    }
}
