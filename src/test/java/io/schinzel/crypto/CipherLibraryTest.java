package io.schinzel.crypto;

import io.schinzel.basicutils.FunnyChars;
import io.schinzel.basicutils.RandomUtil;
import io.schinzel.basicutils.UTF8;
import io.schinzel.crypto.cipher.ICipher;
import io.schinzel.crypto.cipher.NoCipher;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CipherLibraryTest {

    @Test
    public void getSingleton_CallingMethodTwice_ShouldBeSameInstance() {
        CipherLibrary singleton = CipherLibrary.getSingleton();
        assertThat(singleton).isEqualTo(CipherLibrary.getSingleton());
    }


    @Test
    public void encryptDecrypt_FunnyChars_OutputSameAsInput() {
        CipherLibrary cipherLibrary = CipherLibrary.create()
                .addCipher(1, new NoCipher());
        for (FunnyChars funnyChars : FunnyChars.values()) {
            String encryptedString = cipherLibrary.encrypt(1, funnyChars.getString());
            String actual = cipherLibrary.decrypt(encryptedString);
            assertEquals(funnyChars.getString(), actual);
        }
    }


    @Test
    public void encrypt_TwoDifferentCiphersAdded_CorrectCipherIsUsedToEncrypt() {
        ICipher mockCipher1 = mock(ICipher.class);
        when(mockCipher1.encrypt(any(byte[].class))).then(i -> "one_" + UTF8.getString(i.getArgument(0)));
        ICipher mockCipher2 = mock(ICipher.class);
        when(mockCipher2.encrypt(any(byte[].class))).then(i -> "two_" + UTF8.getString(i.getArgument(0)));
        CipherLibrary cipherLibrary = CipherLibrary.create()
                .addCipher(1, mockCipher1)
                .addCipher(2, mockCipher2);
        String encryptedString = cipherLibrary.encrypt(1, "my_first_string");
        assertEquals("v1_one_my_first_string", encryptedString);
        encryptedString = cipherLibrary.encrypt(2, "my_second_string");
        assertEquals("v2_two_my_second_string", encryptedString);
    }


    @Test
    public void decrypt_TwoDifferentCiphersAdded_CorrectCipherIsUsedToDecrypt() {
        ICipher mockCipher1 = mock(ICipher.class);
        when(mockCipher1.decrypt(anyString())).then(i -> i.getArgument(0).toString().substring(7));
        ICipher mockCipher2 = mock(ICipher.class);
        when(mockCipher2.decrypt(anyString())).then(i -> i.getArgument(0).toString().substring(5));
        CipherLibrary cipherLibrary = CipherLibrary.create()
                .addCipher(1, mockCipher1)
                .addCipher(2, mockCipher2);
        String decryptedString = cipherLibrary.decrypt("v1_prefix_my_first_string");
        assertEquals("my_first_string", decryptedString);
        decryptedString = cipherLibrary.decrypt("v2_bapp_my_second_string");
        assertEquals("my_second_string", decryptedString);
    }


    @Test
    public void addCipher_CipherVersionAlreadyExists_ThrowsException() {
        final ICipher mockCipher = mock(ICipher.class);
        final CipherLibrary cipherLibrary = CipherLibrary.create()
                .addCipher(1, mockCipher);
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> cipherLibrary
                .addCipher(1, mockCipher));
    }


    @Test
    public void encrypt_NoCypherWithArgumentVersion_ThrowsException() {
        CipherLibrary cipherLibrary = CipherLibrary.create().addCipher(1, mock(ICipher.class));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                cipherLibrary.encrypt(123, "my_string"));
    }


    @Test
    public void decrypt_NoPrefix_ThrowsException() {
        CipherLibrary cipherLibrary = CipherLibrary.create().addCipher(1, mock(ICipher.class));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                cipherLibrary.decrypt("my_string"));
    }


    @Test
    public void decrypt_NoSuchCipherVersion_ThrowsException() {
        CipherLibrary cipherLibrary = CipherLibrary.create()
                .addCipher(1, mock(ICipher.class));
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                cipherLibrary.decrypt("v123_my_string"));
    }


    @Test
    public void decryptAsByteArray_RandomString_SameAsArgumentString() {
        String clearText = RandomUtil.getRandomString(10);
        CipherLibrary cipherLibrary = CipherLibrary.create()
                .addCipher(1, new NoCipher());
        String encryptedStringWithVersionPrefix = cipherLibrary.encrypt(1, clearText);
        byte[] decryptedBytes = cipherLibrary.decryptToByteArray(encryptedStringWithVersionPrefix);
        String decryptedString = UTF8.getString(decryptedBytes);
        assertThat(decryptedString).isEqualTo(clearText);
    }

}