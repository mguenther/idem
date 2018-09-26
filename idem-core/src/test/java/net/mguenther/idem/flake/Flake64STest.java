package net.mguenther.idem.flake;

import net.mguenther.idem.encoder.Base62Encoder;
import net.mguenther.idem.provider.LinearTimeProvider;
import net.mguenther.idem.provider.StaticWorkerIdProvider;
import org.junit.Test;

public class Flake64STest {

    @Test
    public void getIdShouldYieldGeneratedStringValue() {

        final Flake64S flake64 = new Flake64S(
                new LinearTimeProvider(),
                new StaticWorkerIdProvider("deukalion"),
                new Base62Encoder());

        for (int i = 0; i < 10; i++) {
            System.out.println(flake64.nextId());
        }
    }
}
