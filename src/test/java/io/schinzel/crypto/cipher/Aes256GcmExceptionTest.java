package io.schinzel.crypto.cipher;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class Aes256GcmExceptionTest {

    @Test
    public void constructor_AnyString_CryptoExceptionThrown() {
        Throwable thrown = catchThrowable(() -> {
            throw new Aes256GcmException("My message");
        });
        assertThat(thrown)
                .isInstanceOf(Aes256GcmException.class)
                .hasMessageContaining("My message");
    }

}