package net.mguenther.idem.encoder;

import net.mguenther.idem.Encoder;

import java.nio.ByteBuffer;

/**
 * Implements the encoding part from a {@code java.nio.ByteBuffer} to a String that conforms
 * to Base62 as described in the paper <i>A Secure, Lossless, and Compressed Base62 Encoding</i>
 * by He et al.
 *
 * Please note that a corresponding {@code Decoder} *must* use the same alphabet as was used
 * during encoding. Otherwise, decoding data will fail.
 *
 * The runtime complexity of the implementation is linear wrt. the logarithmic complexity
 * of the given data.
 *
 * This implementation is Thread-safe, since it does not hold any shared mutable state.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class Base62Encoder implements Encoder<ByteBuffer, String> {

    /**
     * Our approach operates on single bytes taken from a {@code ByteBuffer}.
     * However, working with {@code byte}  as a primitive data type is quite tedious
     * (since they are signed) when performing bit manipulations. Therefore, we make
     * use of {@code int} in combination with this bit mask, so that we can map the
     * signed 8-bit-wide {@code byte} to the lower part 8-bit-part of an {@code int},
     * which we can treat as unsigned.
     */
    private static final int BIT_MASK_255 = 0xFF;

    /**
     * This bit mask is used to obtain the least significant bit of an {@code int}.
     */
    private static final int BIT_MASK_LSB = 0x01;
    /**
     * Defines size and order of the used alphabet. The alphabet is the same as in the
     * aforementioned paper by He et al.
     */
    private static final char[] ALPHABET = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    /**
     * Encodes the bits of the given {@code ByteBuffer} according to the Base62-algorithm
     * presented in <i>A Secure, Lossless, and Compressed Base62 Encoding</i> by He et al.
     *
     * The implementation performs bit-wise reads from the given {@code ByteBuffer} and uses
     * a bit-wise sliding-window approach to encode data on-the-fly.
     *
     * @param unencodedData
     *      {@code ByteBuffer} which holds the data that ought to be encoded
     * @return {
     *      @code String} in Base62-compliant representation using {@code alphabet}
     */
    @Override
    public String encode(final ByteBuffer unencodedData) {
        final int maxBytes = unencodedData.limit();
        final StringBuilder sb = new StringBuilder();

        int bitSource = nextByte(unencodedData);
        int unencodedBits = bitSource >> 2; // init unencodedBits with first 6 bits
        int bitIndex = 1; // initialized to 7, minus the 6 bits we already read
        int bitsRead = 6;

        boolean carryOverLsb = false;

        while (!bitsExhausted(bitsRead, maxBytes)) {

            carryOverLsb = appendBitsAsBase62Character(unencodedBits, sb);

            int bitsForAlignment;

            if (carryOverLsb) {
                // if we encoded only 5 bits during the last pass, we have a single bit left
                // that we need to deal with. hence, we must only read the next 5 bits in
                // order to maintain the 6-bit-width alignment
                unencodedBits &= BIT_MASK_LSB;
                bitsForAlignment = 5;
            } else {
                // if we encoded the regular 6 bits during the last pass, we have no bit left
                // to deal with and can continue to read the next 6 bits
                unencodedBits = 0;
                bitsForAlignment = 6;
            }

            for (int i = 0; i < bitsForAlignment; i++) {
                unencodedBits = appendBit(bitSource, unencodedBits, bitIndex);
                bitsRead++;
                bitIndex--;
                // check if we have to switch to the next byte
                if (bitIndex == -1) {
                    if (bitsExhausted(bitsRead, maxBytes)) {
                        break;
                    }
                    bitSource = nextByte(unencodedData);
                    bitIndex = 7;
                }
            }
        }

        // add all remaining bits from the bit source to unencoded bits,
        // append from right
        while (bitIndex >= 0) {
            unencodedBits = appendBit(bitSource, unencodedBits, bitIndex);
            bitIndex--;
        }

        // perform the last encode and ensure that we don't forget
        // a possible carry over LSB
        carryOverLsb = appendBitsAsBase62Character(unencodedBits, sb);

        if (carryOverLsb) {
            unencodedBits &= BIT_MASK_LSB;
            appendBitsAsBase62Character(unencodedBits, sb);
        }

        return sb.toString();
    }

    /**
     * Fetches the next byte from {@code byteSource} and maps its bits onto the
     * lower 8-bits of an {@code int}.
     */
    private int nextByte(final ByteBuffer byteSource) {
        return (int) byteSource.get() & BIT_MASK_255;
    }

    /**
     * Left-shifts {@code to} and appends the bit at {@code index} of {@code from}
     * to the LSB of {@code to}.
     */
    private int appendBit(final int from, final int to, final int index) {
        return to << 1 | ((from >> index) & BIT_MASK_LSB);
    }

    /**
     * @return
     *      {@code true} if we have read all bits from the byte source,
     *      {@code false} otherwise
     */
    private boolean bitsExhausted(final int bitsRead, final int maxBytes) {
        return bitsRead >= maxBytes * 8;
    }

    /**
     * Maps the lower 6 bits of {@code bits} (the most significant two bits are
     * always 0) to the Base62 alphabet.
     *
     * Treats values 61 (0x3D) and 62 (0x3E) as special values. If we encounter
     * a special value, we only take 5 bits into account when mapping to the
     * Base62 alphabet. This leaves a carry-over bit, which we have to signal
     * back to the caller, so that the algorithm can properly take care of the
     * carry-over bit during the next pass.
     */
    private boolean appendBitsAsBase62Character(final int bits, final StringBuilder sb) {
        boolean returnValue;
        char c;
        if (bits < 60) {
            c = ALPHABET[bits];
            returnValue = false;
        } else if (bits < 62) {
            c = ALPHABET[60];
            returnValue = true;
        } else {
            c = ALPHABET[61];
            returnValue = true;
        }
        sb.append(c);
        return returnValue;
    }
}
