package io.schinzel.crypto.cipher;

/**
 * The purpose of this class is to throw a custom exception instead of a RuntimeException.
 * This as to avoid sonar lint warnings about not using generic exception.
 */
@SuppressWarnings("serial")
class Aes256GcmException extends RuntimeException {
    Aes256GcmException(String message) {
        super(message);
    }

}
