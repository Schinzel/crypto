
package io.schinzel.crypto;

import io.schinzel.basicutils.thrower.Thrower;
import io.schinzel.crypto.hash.IHash;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * The purpose of this class is to hold a set of hash functions.
 * <p>
 * Created by schinzel on 2017-04-30.
 */
@SuppressWarnings("WeakerAccess")
public class HashLibrary {
    /** Singleton instance */
    private static final HashLibrary SINGLETON_INSTANCE = new HashLibrary();
    @Getter(AccessLevel.PACKAGE)
    Map<Integer, IHash> hashes = new HashMap<>();


    /**
     * Package private so that only create method is used.
     */
    HashLibrary() {
    }


    /**
     * @return A new instance.
     */
    public static HashLibrary create() {
        return new HashLibrary();
    }


    /**
     * @return A singleton instance
     */
    public static HashLibrary getSingleton() {
        return SINGLETON_INSTANCE;
    }

    /**
     * @param version The version of the hash to add
     * @param hash    The hash to add
     * @return This for chaining
     */
    public HashLibrary addHash(Integer version, IHash hash) {
        boolean hashVersionExists = this.getHashes().containsKey(version);
        Thrower.throwIfTrue(hashVersionExists)
                .message("Cannot add hash to HashLibrary. This as there already exists a hash with version " + version);
        this.getHashes().put(version, hash);
        return this;
    }


    /**
     * Hash the argument string with the hashes function that has the argument version.
     *
     * @param version   The version of the hash to use
     * @param clearText A clear text string to hash
     * @return The argument string hashed with a version prefix.
     */
    public String hash(Integer version, String clearText) {
        Thrower.throwIfFalse(this.getHashes().containsKey(version))
                .message("Cannot hashes as there is no hashes with version " + version);
        IHash hash = this.getHashes().get(version);
        return VersionString.addVersionPrefix(version, hash.hash(clearText));
    }


    /**
     * @param clearText               A clear text string to compare to the argument hashed string.
     * @param hashedStringWithVersion The hashed string to compare with the argument clear text
     *                                string. Example: v1_$2a$10$7QFT2oVQguvBBHM1czu4G.1SeUphAgIygkGu5AtzxHK3Swfrt6rXS
     * @return True if the argument clear text string is the same string as was input when
     * the argument hashed string was created.
     */
    public boolean matches(String clearText, String hashedStringWithVersion) {
        Integer version = VersionString.extractVersion(hashedStringWithVersion);
        Thrower.throwIfFalse(this.getHashes().containsKey(version))
                .message("Cannot hashes as there is no hashes with version " + version);
        String hashedString = VersionString.extractString(hashedStringWithVersion);
        return this.getHashes()
                .get(version)
                .matches(clearText, hashedString);
    }


}

