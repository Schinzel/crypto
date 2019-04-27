package io.schinzel.crypto.encoding.base62;

import java.util.Arrays;

class BitOutputStream {
    private final byte[] buffer;
    private int offset = 0;

    BitOutputStream(int capacity) {
        buffer = new byte[capacity / 8];
    }


    void writeBits(int bitsCount, int bits) {
        final int bitNum = offset % 8;
        final int byteNum = offset / 8;

        final int firstWrite = Math.min(8 - bitNum, bitsCount);
        final int secondWrite = bitsCount - firstWrite;

        buffer[byteNum] |= (bits & ((1 << firstWrite) - 1)) << bitNum;
        if (secondWrite > 0) {
            buffer[byteNum + 1] |= (bits >>> firstWrite) & ((1 << secondWrite) - 1);
        }
        offset += bitsCount;
    }


    byte[] toArray() {
        final int newLength = offset / 8;
        return newLength == buffer.length ? buffer : Arrays.copyOf(buffer, newLength);
    }


    int getBitsCountUpToByte() {
        final int currentBit = offset % 8;
        return currentBit == 0 ? 0 : 8 - currentBit;
    }
}
