package io.schinzel.crypto.encoding.base62;

class BitInputStream {
    private final byte[] buffer;
    private int offset = 0;

    BitInputStream(byte[] bytes) {
        this.buffer = bytes;
    }


    void seekBit() {
        offset--;
        if (offset < 0 || offset > buffer.length * 8) {
            throw new IndexOutOfBoundsException();
        }
    }


    int readBits() {
        int bitsCount = 6;
        final int bitNum = offset % 8;
        final int byteNum = offset / 8;

        final int firstRead = Math.min(8 - bitNum, bitsCount);
        final int secondRead = bitsCount - firstRead;

        int result = ((buffer[byteNum] & 0xff) & (((1 << firstRead) - 1) << bitNum)) >>> bitNum;
        if (secondRead > 0 && byteNum + 1 < buffer.length) {
            result |= (buffer[byteNum + 1] & ((1 << secondRead) - 1)) << firstRead;
        }
        offset += bitsCount;
        return result;
    }


    boolean hasMore() {
        return offset < buffer.length * 8;
    }
}
