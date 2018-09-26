package net.mguenther.idem.flake;

import net.mguenther.idem.encoder.LongEncoder;
import net.mguenther.idem.provider.LinearTimeProvider;
import net.mguenther.idem.provider.StaticWorkerIdProvider;
import org.junit.Test;

public class Flake64LTest {

    @Test
    public void getIdShouldYieldGeneratedLongValue() {

        final Flake64L generator = new Flake64L(
                new LinearTimeProvider(),
                new StaticWorkerIdProvider("deukalion"),
                new LongEncoder());

        for (int i = 0; i < 10; i++) {
            System.out.println(generator.nextId());
        }
    }
}
