package net.mguenther.idem.flake;

import net.mguenther.idem.encoder.Base62Encoder;
import net.mguenther.idem.provider.LinearTimeProvider;
import net.mguenther.idem.provider.MacAddressWorkerIdProvider;
import org.junit.Test;

public class Flake128STest {

    @Test
    public void getIdShouldYieldGeneratedStringValue() {

        final Flake128S generator = new Flake128S(
                new LinearTimeProvider(),
                new MacAddressWorkerIdProvider(),
                new Base62Encoder());

        for (int i = 0; i < 10; i++) {
            System.out.println(generator.nextId());
        }
    }
}
