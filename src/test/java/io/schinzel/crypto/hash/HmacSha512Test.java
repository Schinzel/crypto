package io.schinzel.crypto.hash;

import com.google.common.collect.ImmutableList;
import io.schinzel.basicutils.FunnyChars;
import io.schinzel.crypto.encoding.Encoding;
import io.schinzel.crypto.hash.HmacSha512;
import io.schinzel.crypto.hash.IHash;
import org.junit.Assert;
import org.junit.Test;

public class HmacSha512Test {

    @Test
    public void hash_OneStringHashedTwice_TheHashedStringsShouldBeEqual() {
        ImmutableList<io.schinzel.crypto.hash.IHash> hashes = new ImmutableList.Builder<io.schinzel.crypto.hash.IHash>()
                .add(new io.schinzel.crypto.hash.HmacSha512("0123456789abcdef", Encoding.BASE64))
                .add(new io.schinzel.crypto.hash.HmacSha512("0123456789abcdef", Encoding.HEX))
                .build();
        for (IHash hash : hashes) {
            for (FunnyChars funnyChars : FunnyChars.values()) {
                String clearText = funnyChars.getString();
                Assert.assertEquals(hash.hash(clearText), hash.hash(clearText));
            }
        }
    }


    @Test
    public void constructor_NoEncodingSet_EncodingShouldBeHex() {
        io.schinzel.crypto.hash.HmacSha512 hmacSha512 = new HmacSha512("0123456789abcdef");
        Assert.assertEquals(Encoding.HEX, hmacSha512.mEncoding);

    }


}