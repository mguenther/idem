package net.mguenther.idem.flake;

import net.mguenther.idem.encoder.Encoder;
import net.mguenther.idem.provider.TimeProvider;
import net.mguenther.idem.provider.WorkerIdProvider;
import net.mguenther.idem.sequence.SequenceNumber;

import java.nio.ByteBuffer;

/**
 * This is an implementation of a Flake ID that is 64-bit-wide and is encoded into a
 * {@code java.lang.Long} value.
 *
 * @see Flake64 for further reference on the bitwise-distribution used for this Flake ID
 */
public class Flake64L extends Flake64<Long>{

    private final Encoder<ByteBuffer, Long> encoder;

    public Flake64L(final TimeProvider timeProvider,
                    final WorkerIdProvider workerIdProvider,
                    final Encoder<ByteBuffer, Long> encoder) {
        super(timeProvider, workerIdProvider);
        this.encoder = encoder;
    }

    @Override
    public Long nextId() {

        final SequenceNumber sequenceNumber = getSequence().nextSequenceNumber();
        final ByteBuffer bb = (ByteBuffer) toByteBuffer(sequenceNumber);

        return encoder.encode(bb);
    }
}
