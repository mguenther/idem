package net.mguenther.idem.benchmark;

import net.mguenther.idem.encoder.LongEncoder;
import net.mguenther.idem.flake.Flake64L;
import net.mguenther.idem.provider.LinearTimeProvider;
import net.mguenther.idem.provider.StaticWorkerIdProvider;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State
public class Flake64LBenchmark {

    private Flake64L flake;

    public Flake64LBenchmark() {
        this.flake = new Flake64L(
                new LinearTimeProvider(),
                new StaticWorkerIdProvider("deukalion"),
                new LongEncoder());
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void generateId() {
        flake.nextId();
    }
}
