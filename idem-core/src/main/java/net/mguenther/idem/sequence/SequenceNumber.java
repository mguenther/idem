package net.mguenther.idem.sequence;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public class SequenceNumber {

    private final long timestamp;
    private final byte[] sequenceNumber;

    @Override
    public String toString() {
        return "SequenceNumber{" + "timestamp=" + timestamp + ", sequenceNumber=" + Arrays.toString(sequenceNumber) + '}';
    }
}
