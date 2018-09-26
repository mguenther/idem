package net.mguenther.idem.flake;

import net.mguenther.idem.IdGenerator;
import net.mguenther.idem.encoder.Encoder;
import net.mguenther.idem.provider.TimeProvider;
import net.mguenther.idem.provider.WorkerIdProvider;
import net.mguenther.idem.sequence.Sequence;
import net.mguenther.idem.sequence.SequenceConfig;
import net.mguenther.idem.sequence.SequenceFactory;
import net.mguenther.idem.sequence.SequenceNumber;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static net.mguenther.idem.sequence.SequenceConfig.sequenceProviderConfig;

public class Flake128S implements IdGenerator<String> {

    public static final int MAX_SEQUENCE_NUMBERS = 65536; // 2^16

    private final Encoder<ByteBuffer, String> encoder;

    private final byte[] workerId;

    private final Sequence sequence;

    public Flake128S(final TimeProvider timeProvider,
                     final WorkerIdProvider workerIdProvider,
                     final Encoder<ByteBuffer, String> encoder) {
        final SequenceConfig config = sequenceProviderConfig(MAX_SEQUENCE_NUMBERS).withTimeProvider(timeProvider).build();
        this.encoder = encoder;
        this.workerId = Arrays.copyOf(workerIdProvider.getWorkerId(), 6);
        this.sequence = SequenceFactory.INSTANCE.getSequenceProvider(workerId, config);
    }

    @Override
    public String nextId() {
        final SequenceNumber sequenceNumber = sequence.nextSequenceNumber();
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
