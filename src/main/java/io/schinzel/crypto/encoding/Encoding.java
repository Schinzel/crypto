package io.schinzel.crypto.encoding;


import com.google.common.io.BaseEncoding;
import io.schinzel.crypto.encoding.base62.Base62;
import lombok.SneakyThrows;

import java.util.Base64;

/**
 * Encodes data and decodes encoded string.
 * <p>
 * Created by schinzel on 2017-04-29.
 */
public enum Encoding implements IEncoding {
    BASE64 {
        @Override
        public String encode(byte[] b) {
            return Base64.getEncoder().encodeToString(b);
        }


        @Override
        public byte[] decode(String str) {
            return Base64.getDecoder().decode(str);
        }
    },
    BASE62 {
        @Override
        public String encode(byte[] b) {
            return Base62.encode(b);
        }


        @Override
        public byte[] decode(String str) {
            return Base62.decode(str);
        }
    },
    /** Hex encoding aka Base16 */
    HEX {
        @Override
        public String encode(byte[] b) {
            return BaseEncoding.base16().lowerCase().encode(b);
        }


        @SneakyThrows
        @Override
        public byte[] decode(String str) {
            return BaseEncoding.base16().lowerCase().decode(str);
        }

    }
}
