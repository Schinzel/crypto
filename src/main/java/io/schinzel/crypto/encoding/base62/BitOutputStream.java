package io.schinzel.crypto.encoding.base62;

import java.util.Arrays;

class BitOutputStream {
    private final byte[] mBuffer;
    private int mOffset = 0;

    BitOutputStream(int capacity) {
        mBuffer = new byte[capacity / 8];
    }


    void writeBits(int bitsCount, int bits) {
        final int bitNum = mOffset % 8;
        final int byteNum = mOffset / 8;

        final int firstWrite = Math.min(8 - bitNum, bitsCount);
        final int secondWrite = bitsCount - firstWrite;

        mBuffer[byteNum] |= (bits & ((1 << firstWrite) - 1)) << bitNum;
        if (secondWrite > 0) {
            mBuffer[byteNum + 1] |= (bits >>> firstWrite) & ((1 << secondWrite) - 1);
        }
        mOffset += bitsCount;
    }


    byte[] toArray() {
        final int newLength = mOffset / 8;
        return newLength == mBuffer.length ? mBuffer : Arrays.copyOf(mBuffer, newLength);
    }


    int getBitsCountUpToByte() {
        return BitOutputStream.getBitsCountUpToByte(mOffset);
    }

    static int getBitsCountUpToByte(int offset) {
        final int currentBit = offset % 8;
        return currentBit == 0 ? 0 : 8 - currentBit;
    }
}
