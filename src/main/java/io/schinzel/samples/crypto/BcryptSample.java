package io.schinzel.samples.crypto;


import io.schinzel.crypto.hash.Bcrypt;
import io.schinzel.crypto.hash.IHash;

public class BcryptSample {
    public static void main(String[] args) {
        String clearText = "my string";
        IHash hash = new Bcrypt();
        String hashedText1 = hash.hash(clearText);
        String hashedText2 = hash.hash(clearText);
        System.out.println("Hash1: " + hashedText1);
        System.out.println("Hash1 matches: " + hash.matches(clearText, hashedText1));
        System.out.println("Hash2: " + hashedText2);
        System.out.println("Hash2 matches: " + hash.matches(clearText, hashedText2));
    }
}
