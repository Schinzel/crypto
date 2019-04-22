package io.schinzel.crypto.encoding;

import com.google.common.collect.ImmutableList;
import io.schinzel.basicutils.FunnyChars;
import io.schinzel.basicutils.UTF8;
import io.schinzel.basicutils.file.ResourceReader;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;

public class EncodingTest {


    @Test
    public void encodeDecode_FunnyCharsAllEncodings_InputSameAsOutput() {
        //Go through all encodings
        for (IEncoding encoding : Encoding.values()) {
            //Go through all sets of funny chars
            for (FunnyChars funnyChars : FunnyChars.values()) {
                String funnyString = funnyChars.getString();
                byte[] funnyBytes = UTF8.getBytes(funnyString);
                String encoded = encoding.encode(funnyBytes);
                byte[] decoded = encoding.decode(encoded);
                String actual = UTF8.getString(decoded);
                assertEquals(funnyString, actual);
            }
        }
    }


    @Test
    public void encodeDecode_MultipleFileFormatsAllEncodings_DecodedFileIsSameAsSameAsInput() {
        ImmutableList<String> extensionList = new ImmutableList.Builder<String>()
                .add("doc")
                .add("gif")
                .add("jpg")
                .add("pdf")
                .add("jpg")
                .add("jpg")
                .build();
        //Go through all encodings
        for (IEncoding encoding : Encoding.values()) {
            //Go through all files types
            for (String fileExtension : extensionList) {
                byte[] fileContent = ResourceReader.read("file." + fileExtension).getByteArray();
                String encodedFileContent = encoding.encode(fileContent);
                byte[] decodedFileContent = encoding.decode(encodedFileContent);
                assertThat(fileContent).isEqualTo(decodedFileContent);
            }
        }
    }
    

    @Test
    public void encode_Base64HardcodedValue_HardcodedOutput() {
        String actual = Encoding.BASE64.encode(UTF8.getBytes("åäö"));
        String expected = "w6XDpMO2";
        assertEquals(expected, actual);
    }


    @Test
    public void encode_HexHardcodedValue_HardcodedOutput() {
        String actual = Encoding.HEX.encode(UTF8.getBytes("åäö"));
        String expected = "c3a5c3a4c3b6";
        assertEquals(expected, actual);
    }


    @Test
    public void encode_Base16OddNumberOfChars_ThrowsException() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() ->
                Encoding.HEX.decode("a")).withMessageContaining("Invalid input length ");
    }

}