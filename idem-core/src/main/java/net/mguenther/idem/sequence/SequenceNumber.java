package net.mguenther.idem.sequence;

import java.util.Arrays;

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
