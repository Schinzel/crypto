package io.schinzel.crypto.cipher;

import io.schinzel.basicutils.RandomUtil;
import io.schinzel.crypto.cipher.NoCipher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.*;


public class NoCipherTest {

    @Test
    public void create_NoArgs_NoCipherInstance() {
        assertThat(io.schinzel.crypto.cipher.NoCipher.create().getClass().getSimpleName())
                .isEqualTo(io.schinzel.crypto.cipher.NoCipher.class.getSimpleName());
    }


    @Test
    public void encrypt_RandomString_SameString() {
        String clearText = RandomUtil.getRandomString(20);
        String encrypted = new io.schinzel.crypto.cipher.NoCipher().encrypt(clearText);
        assertEquals(clearText, encrypted);
    }


    @Test
    public void decrypt_RandomString_SameString() {
        String randomString = RandomUtil.getRandomString(20);
        String decrypted = new NoCipher().decrypt(randomString);
        assertEquals(randomString, decrypted);
    }
}