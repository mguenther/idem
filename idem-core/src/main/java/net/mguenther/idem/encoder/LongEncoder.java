package net.mguenther.idem.encoder;

import java.nio.ByteBuffer;

public class LongEncoder implements Encoder<ByteBuffer, Long> {

    @Override
    public Long encode(final ByteBuffer unencodedData) {

        if (unencodedData.limit() != 8) {
            throw new EncoderException("The given ByteBuffer contains too much data to fit into a Long-based representation.");
        }

        return unencodedData.asLongBuffer().get();
    }
}
