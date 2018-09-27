package net.mguenther.idem.flake;

import net.mguenther.idem.encoder.Base62Encoder;
import net.mguenther.idem.encoder.Encoder;
import net.mguenther.idem.provider.TimeProvider;
import net.mguenther.idem.provider.WorkerIdProvider;
import net.mguenther.idem.sequence.SequenceNumber;

import java.nio.ByteBuffer;

/**
 * This is an implementation of a Flake ID that is 128-bit-wide and is encoded into a
 * {@code java.lang.String} value.
 *
 * @see Flake64 for further reference on the bitwise-distribution used for this Flake ID
 */
public class Flake64S extends Flake64<String> {

    private final Encoder<ByteBuffer, String> encoder;

    /**
     * Uses the default Base62-encoder for {@code String}-based target representations that
     * comes with idem.
     */
    public Flake64S(final TimeProvider timeProvider,
                    final WorkerIdProvider workerIdProvider) {
        this(timeProvider, workerIdProvider, new Base62Encoder());
    }

    public Flake64S(final TimeProvider timeProvider,
                    final WorkerIdProvider workerIdProvider,
                    final Encoder<ByteBuffer, String> encoder) {
        super(timeProvider, workerIdProvider);
        this.encoder = encoder;
    }

    @Override
    public String nextId() {

        final SequenceNumber sequenceNumber = getSequence().nextSequenceNumber();
        final ByteBuffer bb = (ByteBuffer) toByteBuffer(sequenceNumber);

        return encoder.encode(bb);
    }
}
