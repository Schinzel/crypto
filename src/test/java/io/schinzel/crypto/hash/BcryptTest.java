package io.schinzel.crypto.hash;

import io.schinzel.crypto.hash.Bcrypt;
import io.schinzel.crypto.hash.IHash;
import org.junit.Assert;
import org.junit.Test;

public class BcryptTest {

    @Test
    public void hash_OneStringHashedTwice_TheHashedStringsShouldNotBeEqual() {
        IHash hash = new io.schinzel.crypto.hash.Bcrypt(4);
        String clearText = "This is a string";
        Assert.assertNotEquals(hash.hash(clearText), hash.hash(clearText));
    }


    @Test
    public void constructor_NoArguments_IterationsShouldBe10() {
        io.schinzel.crypto.hash.Bcrypt bcrypt = new Bcrypt();
        Assert.assertEquals(10, bcrypt.mIterations);
    }
}