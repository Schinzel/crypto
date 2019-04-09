package io.schinzel.samples.crypto;

import io.schinzel.basicutils.crypto.cipher.Aes256Gcm;
import io.schinzel.basicutils.crypto.cipher.ICipher;
import io.schinzel.basicutils.crypto.CipherLibrary;

/**
 * Using an enum can be beneficial for readability.
 */
public class CipherLibraryEnumSample {
    public static void main(String[] args) {
        String encrypted = MyCipher.MOBILE_NUMBER.encrypt("12345");
        System.out.println(encrypted);
        System.out.println(MyCipher.decrypt(encrypted));
    }


    enum MyCipher {
        MOBILE_NUMBER(1, new Aes256Gcm("cBG7TyXVtKh8W6ekizzNcZu2o3euNEge")),
        EMAIL_ADDRESS(2, new Aes256Gcm("pT6fwWD2ECoXQjBAPd8bn4UQZHnKv6wR")),
        SSN(3, new Aes256Gcm("odGqdQ79eGYNjE27fTE3GBpMgeYxKMBT"));

        private final int mCipherVersion;


        MyCipher(Integer version, ICipher cipher) {
            CipherLibrary.getSingleton().addCipher(version, cipher);
            this.mCipherVersion = version;

        }


        String encrypt(String clearTextString) {
            return CipherLibrary.getSingleton().encrypt(mCipherVersion, clearTextString);
        }


        static String decrypt(String encryptedString) {
            return CipherLibrary.getSingleton().decrypt(encryptedString);
        }
    }


}
