package io.schinzel.crypto.encoding.base62;

import io.schinzel.basicutils.thrower.Thrower;

/**
 * Code is from here https://github.com/glowfall/base62
 * <p>
 * The advantage of Base62 over Base64 it that it only uses alphanumeric characters.
 * <p>
 * It is a fast implementation.
 * 10 000 encodes and decodes of strings with a random length between 1 and 500 chars took
 * approximately 100 ms on a 2016 MacBook Pro 2,6 GHz Intel Core i7.
 * <p>
 * It is about half as fast as Guava's Base16 and Base64 encoding on larger byte arrays.
 * 10 encodes and decodes of a 5 MB png file took on a 2016 MacBook Pro 2,6 GHz Intel Core i7:
 * - Base62 took 1377 ms
 * - Base64 took 586 ms
 * - Base16 took 654 ms
 * <p>
 * The only other base 62 Java repo I found github.com/seruco/base62 was very slow. After more than
 * 3 minutes not a single encode and decode of a 5 MB png file had been completed.
 * <p>
 * Did some code clean up for SonarCloud and IntelliJ warnings, broke out private classes
 * to have their own files and more mainly cosmetic changes.
 * <p>
 * Original JavaDoc below this line
 * **********
 * Provides Base62 encoding and decoding.
 * <p>
 * The Base62 alphabet used by this algorithm in common is equivalent to the Base64 alphabet as defined by RFC 2045.
 * The only exception is a representations for 62 and 63 6-bit values. For that values special encoding is used.
 *
 * @author Pavel Myasnov
 */
public class Base62 {

    Base62() {
    }

    /**
     * This array is a lookup table that translates 6-bit positive integer index values into their "Base62 Alphabet"
     * equivalents as specified in Table 1 of RFC 2045 excepting special characters for 62 and 63 values.
     * <p>
     * Thanks to "commons" project in ws.apache.org for this code.
     * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
     */
    private static final char[] ENCODE_TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    /**
     * This array is a lookup table that translates Unicode characters drawn from the "Base64 Alphabet" (as specified in
     * Table 1 of RFC 2045) into their 6-bit positive integer equivalents. Characters that are not in the Base62
     * alphabet but fall within the bounds of the array are translated to -1.
     * <p>
     * Note that there is no special characters in Base62 alphabet that could represent 62 and 63 values, so they both
     * is absent in this decode table.
     * <p>
     * Thanks to "commons" project in ws.apache.org for this code.
     * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
     */
    private static final byte[] DECODE_TABLE = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
            -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
            -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51
    };

    /** Special mask for the data that should be written in compact 5-bits form */
    private static final int COMPACT_MASK = 0x1E; // 00011110
    /** Mask for extracting 5 bits of the data */
    private static final int MASK_5BITS = 0x1F; // 00011111

    /**
     * Encodes binary data using a Base62 algorithm.
     *
     * @param data binary data to encode
     * @return String containing Base62 characters
     */
    public static String encode(byte[] data) {
        Thrower.throwIfNull(data).message("Cannot encode a null value");
        // Reserving capacity for the worst case when each output character represents compacted 5-bits data
        final StringBuilder sb = new StringBuilder(data.length * 8 / 5 + 1);

        final BitInputStream in = new BitInputStream(data);
        while (in.hasMore()) {
            // Read not greater than 6 bits from the stream
            final int rawBits = in.readBits();

            // For some cases special processing is needed, so _bits_ will contain final data representation needed to
            // form next output character
            final int bits;
            if ((rawBits & COMPACT_MASK) == COMPACT_MASK) {
                // We can't represent all 6 bits of the data, so extract only least significant 5 bits and return for
                // one bit back in the stream
                bits = rawBits & MASK_5BITS;
                in.seekBit();
            } else {
                // In most cases all 6 bits used to form output character
                bits = rawBits;
            }

            // Look up next character in the encoding table and append it to the output StringBuilder
            sb.append(ENCODE_TABLE[bits]);
        }
        return sb.toString();
    }


    /**
     * Decodes a Base62 String into byte array.
     *
     * @param base62String String containing Base62 data
     * @return Array containing decoded data.
     */
    public static byte[] decode(String base62String) {
        Thrower.throwIfNull(base62String).message("Cannot encode a null value");
        final int length = base62String.length();

        // Create stream with capacity enough to fit
        final BitOutputStream out = new BitOutputStream(length * 6);

        final int lastCharPos = length - 1;
        for (int i = 0; i < length; i++) {
            // Obtain data bits from decoding table for the next character
            final int bits = decodedBitsForCharacter(base62String.charAt(i));

            // Determine bits count needed to write to the stream
            final int bitsCount;
            if ((bits & COMPACT_MASK) == COMPACT_MASK) {
                // Compact form detected, write down only 5 bits
                bitsCount = 5;
            } else if (i >= lastCharPos) {
                // For the last character write down all bits that needed for the completion of the stream
                bitsCount = out.getBitsCountUpToByte();
            } else {
                // In most cases the full 6-bits form will be used
                bitsCount = 6;
            }

            out.writeBits(bitsCount, bits);
        }
        return out.toArray();
    }


    static int decodedBitsForCharacter(char character) {
        final int result;
        if (character >= DECODE_TABLE.length || (result = DECODE_TABLE[character]) < 0) {
            throw new IllegalArgumentException("Wrong Base62 symbol found: " + character);
        }
        return result;
    }


}
