package io.schinzel.crypto.encoding.base62;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


public class BitOutputStreamTest {

    @Test
    public void getBitsCountUpToByte_Offset8_0() {
        int output = BitOutputStream.getBitsCountUpToByte(8);
        assertThat(output).isZero();
    }

    @Test
    public void getBitsCountUpToByte_Offset12_4() {
        int output = BitOutputStream.getBitsCountUpToByte(12);
        assertThat(output).isEqualTo(4);
    }

}