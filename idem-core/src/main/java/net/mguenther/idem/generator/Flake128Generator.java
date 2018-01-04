package net.mguenther.idem.generator;

import net.mguenther.idem.Encoder;
import net.mguenther.idem.IdGenerator;
import net.mguenther.idem.SequenceNumber;
import net.mguenther.idem.SequenceProvider;
import net.mguenther.idem.SequenceProviderConfig;
import net.mguenther.idem.SequenceProviderFactory;
import net.mguenther.idem.TimeProvider;
import net.mguenther.idem.WorkerIdProvider;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import static net.mguenther.idem.SequenceProviderConfig.sequenceProviderConfig;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class Flake128Generator implements IdGenerator<String> {

    public static final int MAX_SEQUENCE_NUMBERS = 65536; // 2^16

    private final Encoder<ByteBuffer, String> encoder;

    private final byte[] workerId;

    private final SequenceProvider sequenceProvider;

    public Flake128Generator(final TimeProvider timeProvider,
                             final WorkerIdProvider workerIdProvider,
                             final Encoder<ByteBuffer, String> encoder) {
        final SequenceProviderConfig config = sequenceProviderConfig(MAX_SEQUENCE_NUMBERS).withTimeProvider(timeProvider).build();
        this.encoder = encoder;
        this.workerId = workerIdProvider.getWorkerId();
        this.sequenceProvider = SequenceProviderFactory.INSTANCE.getSequenceProvider(workerId, config);
    }

    @Override
    public String getId() {
        final SequenceNumber sequenceNumber = sequenceProvider.nextSequenceNumber();
        final ByteBuffer bb = (ByteBuffer) toByteBuffer(sequenceNumber.getTimestamp(), workerId, sequenceNumber.getSequenceNumber());
        return encoder.encode(bb);
    }

    private Buffer toByteBuffer(final long timestamp, final byte[] workerId, final byte[] sequenceNumber) {
        return ByteBuffer.allocate(8 + workerId.length + sequenceNumber.length)
                .putLong(timestamp)
                .put(workerId)
                .put(sequenceNumber)
                .clear();
    }
}
