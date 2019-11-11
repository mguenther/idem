package net.mguenther.idem.benchmark;

import net.mguenther.idem.encoder.LongEncoder;
import net.mguenther.idem.flake.Flake64L;
import net.mguenther.idem.provider.LinearTimeProvider;
import net.mguenther.idem.provider.StaticWorkerIdProvider;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class Flake64LBenchmark {

    private Flake64L flake;

    @Setup
    public void init() {
        this.flake = new Flake64L(
                new LinearTimeProvider(),
                new StaticWorkerIdProvider("deukalion"),
                new LongEncoder());
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void generateId() {
        flake.nextId();
    }
}
