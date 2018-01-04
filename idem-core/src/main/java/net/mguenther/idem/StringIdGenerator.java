package net.mguenther.idem;

import net.mguenther.idem.encoder.Encoder;
import net.mguenther.idem.provider.SynchronizedSequenceProvider;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class StringIdGenerator implements IdGenerator<String> {

    private final TimeProvider timeProvider;
    private final WorkerIdProvider workerIdProvider;
    private final SynchronizedSequenceProvider sequenceProvider;
    private final Encoder<ByteBuffer, String> encoder;

    public StringIdGenerator(final TimeProvider timeProvider,
                             final WorkerIdProvider workerIdProvider,
                             final SynchronizedSequenceProvider sequenceProvider,
                             final Encoder<ByteBuffer, String> encoder) {
        this.timeProvider = timeProvider;
        this.workerIdProvider = workerIdProvider;
        this.sequenceProvider = sequenceProvider;
        this.encoder = encoder;
    }

    @Override
    public String getId() {
        final long timestamp = timeProvider.getTimestamp();
        final byte[] workerId = workerIdProvider.getWorkerId();
        final byte[] sequenceNumber = sequenceProvider.nextSequenceNumber(timestamp);
        final ByteBuffer bb = (ByteBuffer) toByteBuffer(timestamp, workerId, sequenceNumber);
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
