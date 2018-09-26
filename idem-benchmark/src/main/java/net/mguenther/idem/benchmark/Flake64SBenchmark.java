package net.mguenther.idem.benchmark;

import net.mguenther.idem.encoder.Base62Encoder;
import net.mguenther.idem.flake.Flake64S;
import net.mguenther.idem.provider.LinearTimeProvider;
import net.mguenther.idem.provider.StaticWorkerIdProvider;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State
public class Flake64SBenchmark {

    private Flake64S flake;

    public Flake64SBenchmark() {
        this.flake = new Flake64S(
                new LinearTimeProvider(),
                new StaticWorkerIdProvider("deukalion"),
                new Base62Encoder());
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void generateId() {
        flake.nextId();
    }
}
