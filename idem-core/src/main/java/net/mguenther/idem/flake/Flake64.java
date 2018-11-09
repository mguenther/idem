package net.mguenther.idem.flake;

import net.mguenther.idem.IdGenerator;
import net.mguenther.idem.provider.TimeProvider;
import net.mguenther.idem.provider.WorkerIdProvider;
import net.mguenther.idem.sequence.Sequence;
import net.mguenther.idem.sequence.SequenceNumber;
import net.mguenther.idem.sequence.SequenceConfig;
import net.mguenther.idem.sequence.SequenceFactory;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * This is an implementation of a Flake ID that is 64-bit-wide and comprises:
 * <ul>
 *     <li>Bit 63: The MSB (most-significant-bit) is set to 0 due to the fact that a byte is signed.</li>
 *     <li>Bits 62-21: the lower 41 bits of a 64-bit-wide timestamp in milliseconds since the epoch</li>
 *     <li>Bits 21-12: 10-bit-wide worker process ID</li>
 *     <li>Bits 11-0: 12-bit-wide sequence number</li>
 * </ul>
 *
 * This gives us possible 2^12=4096 Flake IDs per millisecond and thus 4096000 Flake IDs per seconds,
 * which seems a reasonable fit even for large scale operations.
 *
 * @param <T>
 *     A Flake ID requires a typed representation. Instead of using the underlying {@code byte[]} as
 *     an ID, an {@code Encoder<T>} ensures that the generated Flake ID will be represented by a
 *     human- and machine-readable type. See implementations of this class for further information.
 */
abstract class Flake64<T> implements IdGenerator<T> {

    private static final long WORKER_ID_LOWER_BITS = 0x3FFL;

    static final long TIMESTAMP_LOWER_BITS = 0x1FFFFFFFFFFL;

    static final long SEQUENCE_LOWER_BITS = 0xFFFL;

    private final Sequence sequence;

    private final byte[] workerId;

    Flake64(final TimeProvider timeProvider,
            final WorkerIdProvider workerIdProvider) {
        final SequenceConfig config = SequenceConfig.create((int) Math.pow(2, 12))
                .withTimeProvider(timeProvider)
                .build();
        this.workerId = workerIdProvider.getWorkerId();
        this.sequence = SequenceFactory.INSTANCE.getSequenceProvider(workerId, config);
    }

    Buffer toByteBuffer(final SequenceNumber sequenceNumber) {
        final long timestamp = sequenceNumber.getTimestamp();
        final byte[] seqNoBytes = sequenceNumber.getSequenceNumber();

        final long id =
                (timestamp & TIMESTAMP_LOWER_BITS) << 22 |
                (ByteBuffer.wrap(workerId).getInt() & WORKER_ID_LOWER_BITS) << 12 |
                (ByteBuffer.wrap(seqNoBytes).getShort() & SEQUENCE_LOWER_BITS);
        return ByteBuffer.allocate(8).putLong(id).clear();
    }

    Sequence getSequence() {
        return sequence;
    }
}

