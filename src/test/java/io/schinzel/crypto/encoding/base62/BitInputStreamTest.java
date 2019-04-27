package io.schinzel.crypto.encoding.base62;

import io.schinzel.basicutils.EmptyObjects;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.*;

public class BitInputStreamTest {

    @Test
    public void seekBit_OffsetMinus1AndEmptyArray_Exception() {
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() ->
                BitInputStream.seekBit(-1, EmptyObjects.EMPTY_BYTE_ARRAY)
        );
    }

    @Test
    public void seekBit_Offset10AndEmptyArray_Exception() {
        assertThatExceptionOfType(IndexOutOfBoundsException.class).isThrownBy(() ->
                BitInputStream.seekBit(10, EmptyObjects.EMPTY_BYTE_ARRAY)
        );
    }

}