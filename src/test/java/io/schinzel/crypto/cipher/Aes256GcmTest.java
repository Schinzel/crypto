package io.schinzel.crypto.cipher;

import io.schinzel.basicutils.FunnyChars;
import io.schinzel.basicutils.RandomUtil;
import io.schinzel.basicutils.UTF8;
import io.schinzel.basicutils.str.Str;
import io.schinzel.crypto.encoding.Encoding;
import org.junit.Test;

import javax.crypto.Cipher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class Aes256GcmTest {
    private final String mKey = RandomUtil.getRandomString(32);


    @Test
    public void create_KeyArg_HexEncoding() {
        Aes256Gcm aes256Gcm = Aes256Gcm.create(mKey);
        assertThat(aes256Gcm.getEncoding().getClass().getSimpleName()).isEqualTo(Encoding.HEX.getClass().getSimpleName());
    }


    @Test
    public void create_KeyArg_DefaultArgHex() {
        assertThat(Aes256Gcm.create(mKey).getKey()).containsExactly(UTF8.getBytes(mKey));
    }


    @Test
    public void constructor_KeyNonAsciiAndThusNo32Chars_ShouldStillWork() {
        Aes256Gcm aes = new Aes256Gcm("ĄąŚśŹźŻżĄąŚśŹźŻż");
        String clearText = "This is a text";
        assertNotEquals(aes.encrypt(clearText), aes.encrypt(clearText));

    }


    @Test
    public void encrypt_OneStringEncryptedTwice_EncryptedStringsShouldDiffer() {
        Aes256Gcm aes = new Aes256Gcm(mKey);
        String clearText = "This is a text";
        assertNotEquals(aes.encrypt(clearText), aes.encrypt(clearText));
    }


    @Test
    public void constructor_WrongKeySize_ThrowException() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                new Aes256Gcm("ThisIsTheKey")
        );
    }


    @Test
    public void constructor_ByteArrEmptyKey_ThrowException() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                new Aes256Gcm(new byte[]{}, Encoding.BASE64)
        );
    }


    @Test
    public void constructor_ByteArrNullKey_ThrowException() {
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                new Aes256Gcm((byte[]) null, Encoding.BASE64)
        );
    }


    @Test
    public void constructor_NullKey_ThrowException() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> new Aes256Gcm(null))
                .withMessageStartingWith("Argument 'key' cannot be empty");
    }


    @Test
    public void constructor_KeyNot32Long_Exception() {
        byte[] key = {1, 2, 3};
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> new Aes256Gcm(key, Encoding.HEX))
                .withMessageStartingWith("Key length must be 32");
    }


    @Test
    public void constructorOneArg() {
        Aes256Gcm aes = new Aes256Gcm(mKey);
        String expected = "abc";
        String actual = aes.decrypt(aes.encrypt(expected));
        assertEquals(expected, actual);
    }


    @Test
    public void encryptAndDecrypt_FunnyCharsAvailableEncodings_ShouldBeTheSame() {
        for (Encoding encoding : Encoding.values()) {
            Aes256Gcm aes = new Aes256Gcm(mKey, encoding);
            for (FunnyChars funnyChars : FunnyChars.values()) {
                String encrypted = aes.encrypt(funnyChars.getString());
                String decrypted = aes.decrypt(encrypted);
                assertEquals(funnyChars.getString(), decrypted);
            }
        }
    }


    @Test
    public void encryptAndDecrypt_longText_shouldBeTheSame() {
        RandomUtil random = RandomUtil.create(123);
        Str str = Str.create();
        for (int i = 0; i < 1000; i++) {
            str.a(random.getString(100));
        }
        String expected = str.asString();
        Aes256Gcm aes = new Aes256Gcm(mKey);
        String encrypted = aes.encrypt(expected);
        String decrypted = aes.decrypt(encrypted);
        assertEquals(expected, decrypted);
    }


    @Test
    public void crypt_InvalidKeyLength_ThrowException() {
        final byte[] keys = UTF8.getBytes("key");
        final byte[] initVector = UTF8.getBytes("InitVector123456");
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> Aes256Gcm.crypt(new byte[]{1, 2, 3}, Cipher.ENCRYPT_MODE, keys, initVector))
                .withMessageStartingWith("Problems encrypting or decrypting string. Invalid AES key length");
    }


}