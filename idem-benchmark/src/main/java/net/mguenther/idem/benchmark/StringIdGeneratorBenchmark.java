package net.mguenther.idem.benchmark;

import net.mguenther.idem.generator.Flake128Generator;
import net.mguenther.idem.encoder.Base62Encoder;
import net.mguenther.idem.provider.LinearTimeProvider;
import net.mguenther.idem.provider.MacAddressWorkerIdProvider;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
@State
public class StringIdGeneratorBenchmark {

    private final Flake128Generator idGenerator;

    public StringIdGeneratorBenchmark() {
        this.idGenerator = new Flake128Generator(
                new LinearTimeProvider(),
                new MacAddressWorkerIdProvider(),
                new Base62Encoder());
    }

    @GenerateMicroBenchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void generateId() {
        idGenerator.getId();
    }
}
