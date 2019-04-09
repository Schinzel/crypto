package io.schinzel.crypto;

import io.schinzel.basicutils.FunnyChars;
import io.schinzel.basicutils.RandomUtil;
import io.schinzel.crypto.VersionString;
import org.junit.Assert;
import org.junit.Test;

public class VersionStringTest {

    @Test
    public void extractVersion_VersionSetWithAddVersionPrefix_OutputShouldBeSameAsInput() {
        for (FunnyChars funnyChars : FunnyChars.values()) {
            String string = funnyChars.getString();
            int version = RandomUtil.getRandomNumber(1, Integer.MAX_VALUE);
            String s = io.schinzel.crypto.VersionString.addVersionPrefix(version, string);
            Assert.assertEquals(version, (long) io.schinzel.crypto.VersionString.extractVersion(s));

        }
    }


    @Test
    public void extractString_StringSetWithAddVersionPrefix_OutputShoudlBeSameAsInput() {
        for (FunnyChars funnyChars : FunnyChars.values()) {
            String input = funnyChars.getString();
            int version = RandomUtil.getRandomNumber(1, Integer.MAX_VALUE);
            String s = io.schinzel.crypto.VersionString.addVersionPrefix(version, input);
            Assert.assertEquals(input, VersionString.extractString(s));
        }
    }

}