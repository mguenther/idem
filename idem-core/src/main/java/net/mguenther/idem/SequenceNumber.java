package net.mguenther.idem;

import java.util.Arrays;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class SequenceNumber {

    private final long timestamp;
    private final byte[] sequenceNumber;

    public SequenceNumber(final long timestamp, final byte[] sequenceNumber) {
        this.timestamp = timestamp;
        this.sequenceNumber = sequenceNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte[] getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public String toString() {
        return "SequenceNumber{" + "timestamp=" + timestamp + ", sequenceNumber=" + Arrays.toString(sequenceNumber) + '}';
    }
}
