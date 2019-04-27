package io.schinzel.crypto.encoding.base62;

class BitInputStream {
    private final byte[] mBuffer;
    private int mOffset = 0;

    BitInputStream(byte[] bytes) {
        this.mBuffer = bytes;
    }


    void seekBit() {
        mOffset = BitInputStream.seekBit(mOffset, mBuffer);
    }

    static int seekBit(int offset, byte[] buffer) {
        offset--;
        if (offset < 0 || offset > buffer.length * 8) {
            throw new IndexOutOfBoundsException();
        }
        return offset;
    }


    int readBits() {
        int bitsCount = 6;
        final int bitNum = mOffset % 8;
        final int byteNum = mOffset / 8;

        final int firstRead = Math.min(8 - bitNum, bitsCount);
        final int secondRead = bitsCount - firstRead;

        int result = ((mBuffer[byteNum] & 0xff) & (((1 << firstRead) - 1) << bitNum)) >>> bitNum;
        if (secondRead > 0 && byteNum + 1 < mBuffer.length) {
            result |= (mBuffer[byteNum + 1] & ((1 << secondRead) - 1)) << firstRead;
        }
        mOffset += bitsCount;
        return result;
    }


    boolean hasMore() {
        return mOffset < mBuffer.length * 8;
    }
}
