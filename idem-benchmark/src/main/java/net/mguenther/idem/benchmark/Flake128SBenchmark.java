package net.mguenther.idem.benchmark;

import net.mguenther.idem.encoder.Base62Encoder;
import net.mguenther.idem.flake.Flake128S;
import net.mguenther.idem.provider.LinearTimeProvider;
import net.mguenther.idem.provider.MacAddressWorkerIdProvider;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class Flake128SBenchmark {

    private Flake128S flake;

    @Setup
    public void init() {
        this.flake = new Flake128S(
                new LinearTimeProvider(),
                new MacAddressWorkerIdProvider(),
                new Base62Encoder());
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void generateId() {
        flake.nextId();
    }
}
