package net.mguenther.idem.benchmark;

import net.mguenther.idem.encoder.Base62Encoder;
import net.mguenther.idem.flake.Flake128S;
import net.mguenther.idem.provider.LinearTimeProvider;
import net.mguenther.idem.provider.MacAddressWorkerIdProvider;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State
public class Flake128SBenchmark {

    private final Flake128S flake;

    public Flake128SBenchmark() {
        this.flake = new Flake128S(
                new LinearTimeProvider(),
                new MacAddressWorkerIdProvider(),
                new Base62Encoder());
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void generateId() {
        flake.nextId();
    }
}
