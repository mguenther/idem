package net.mguenther.idem;

import net.mguenther.idem.encoder.Base62Encoder;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class Base62EncoderTest {

    private final Base62Encoder encoder = new Base62Encoder();

    @Test
    public void encodeWithSpecialCharacterAtEndPadsCorrectlyToNextCharacter() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer
                .put((byte) 0x07)
                .put((byte) 0x59)
                .put((byte) 0x06)
                .put((byte) 0x61)
                .put((byte) 0x47)
                .put((byte) 0xFE)
                .flip();
        assertThat(encoder.encode(byteBuffer), is("B1kGYUf9A"));
    }

    @Test
    public void encodeShouldYieldValidBase62CharacterSimpleSubstitution() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        byteBuffer
                .put((byte) 0x53)
                .flip();
        assertThat(encoder.encode(byteBuffer), is("UD"));
    }

    @Test
    public void encodeByteWithFiveLeadingSetOnesYieldsCorrectResult() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        byteBuffer
                .put((byte) 0xF9)
                .flip();
        assertThat(encoder.encode(byteBuffer), is("9B"));

        final ByteBuffer byteBuffer2 = ByteBuffer.allocate(1);
        byteBuffer2
                .put((byte) 0xF8)
                .flip();
        assertThat(encoder.encode(byteBuffer2), is("9A"));
    }

    @Test
    public void encodeByteWithFourLeadingOnesYieldsCorrectResult() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        byteBuffer
                .put((byte) 0xF0)
                .flip();
        assertThat(encoder.encode(byteBuffer), is("8A"));
    }

    @Test
    public void encodeByteWithZeroValuedRemainingBitsYieldsCorrectResult() {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        byteBuffer
                .put((byte) 0xF8)
                .flip();
        assertThat(encoder.encode(byteBuffer), is("9A"));
    }

    @Test
    public void encodeWithNoRemainingBitsYieldsCorrectResult() {
        // 6 bytes * 8 bit/byte = 8 symbols * 6 bit/symbol
        // no padding required in this case
        final ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer
                .put((byte) 0x00)
                .put((byte) 0x00)
                .put((byte) 0x00)
                .put((byte) 0x00)
                .put((byte) 0x00)
                .put((byte) 0x01)
                .flip();
        assertThat(encoder.encode(byteBuffer), is("AAAAAAAB"));
    }

    @Test
    public void encodeShouldYieldValidBase62StringForPaperExample() {
        // these 3 bytes are taken from the paper
        // "A Secure, Lossless, and Compressed Base62 Encoding" by
        // He et al.
        // the resulting base62 encoding is different, because
        // He et al. use a different alphabet in the paper.
        final ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        byteBuffer
                .put((byte) 0x53)
                .put((byte) 0xFE)
                .put((byte) 0x92)
                .flip();
        assertThat(encoder.encode(byteBuffer), is("U98kC"));
    }
}
