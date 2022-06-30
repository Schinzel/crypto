# crypto
The purpose of this library is to offer protection of data from prying eyes through encryption and hash functions

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=security_rating)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=coverage)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=bugs)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=code_smells)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=sqale_index)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=ncloc)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)


#### Sample code
```java
Aes256Gcm aes = Aes256Gcm("0123456789abcdef0123456789abcdef");
String encryptedString = aes.encrypt("This is a string");

IHash hash = new Bcrypt();
String hashedText = hash.hash(clearText);

CipherLibrary cipherLibrary = CipherLibrary.create()
        .addCipher(1, new Aes256Gcm("G2Ed3Zs6qDAft8Az7268BE9bWbAzXm2q"))
        .addCipher(2, new Aes256Gcm("uhgZxYLKn92w2B2ZFUtT2y98Z9K89MmX"))
        .addCipher(3, new Aes256Gcm("et4CgMZd99zPodK7QnLDGMV643GE94Q8"));
String encryptedString = cipherLibrary.encrypt(1, "This is a string");
String decryptedString = cipherLibrary.decrypt(encryptedString);
```

#### How to add repo
```xml
<repositories>
	<repository>
		<id>maven-repo.schinzel.io</id>
		<url>>https://s3-eu-west-1.amazonaws.com/maven-repo.schinzel.io/release</url>
	</repository>
</repositories>    

<dependencies>
	<dependency>
		<groupId>io.schinzel</groupId>
		<artifactId>crypto</artifactId>
		<version>1.X.X</version>
	</dependency>
</dependencies>    
```


# Releases
### 1.2.5
_2022-06-30_
- Upgraded dependencies

### 1.2.4
_2021-10-22_
- Upgraded dependencies

### 1.2.3
_2021-10-21_
- Upgraded dependencies

### 1.2.2
_2020-10-22_
- Upgraded dependencies

### 1.2.1
_2020-01-03_
- Upgraded dependencies

### 1.2
_2019-07-24_
- Added Base62 encoding
- Upgraded dependencies

### 1.1
_2019-04-09_
- First release. Extracted from basic-utils and made into its own repo

# Introduction
#### Purpose
The purpose of this library is to offer:
1) top notch protection of data from prying eyes
2) through the use of cryptographic techniques such as encryption and hashing
3) with minimal effort and distraction from the actual business problem being solved.

#### Types of data protection
This library offers three types of protection:
- Protection of sensitive data such as medical records, financial records and so on.
- Protection of passwords.
- Protection of searchable personally identifiable information (PII).


#### Audience
The intended audience of this library want to protect their data but know little to nothing about cryptography.
If you know why a salt is mandatory in a hash, why SHA2 is a poor choice to protect passwords and what an init vector in AES is,
use a library like [Sodium](https://download.libsodium.org/doc/) instead.
If you do not know these things, this library might be for you.

Do note that there are other aspects that are important to IT-security such as data integrity, key management and much more.
For a full fledged solution consider turning to a company that specializes in IT-security.
A great choice for this would be [TrueSec](https://www.truesec.se/international) in Sweden
(written in 2017, the author has no financial nor personal relationship to TrueSec).

## Overview
Ciphers and hashes are used protect data from prying eyes by converting a clear text to a set
seemingly random characters. Ciphers are used when you need to be able to retrieve the original
clear text string. Hashes are used when you do _not_ want to be able to retrieve the original clear
text string. Hashes are typically used to protect passwords.

Note that for more than 128 bits encryption you need to download
"Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files", which can be
found here:
http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html

Put the extracted files in this dir: _${java.home}/jre/lib/security/._
To find java home: _/usr/libexec/java_home -v 1.8_

# Data Protection

## 1 How to protect sensitive data
Examples of data:
- back up files
- financial records
- medical records

This is data that is highly sensitive and there must exist a method to restore the data to clear text.

For this type of data use the cipher AES-256.
If implemented correctly AES-256 is considered unbreakable.
Allegedly US agencies like NSA use AES-256 for documents labeled Top Secret.


```java
Aes256Gcm aes = new Aes256Gcm("0123456789abcdef0123456789abcdef");
String encrypted1 = aes.encrypt("This is a string");
String encrypted2 = aes.encrypt("This is a string");
System.out.println("Encrypted 1: " + encrypted1);
System.out.println("Encrypted 2: " + encrypted2);
System.out.println("Decrypted 1: " + aes.decrypt(encrypted1));
System.out.println("Decrypted 2: " + aes.decrypt(encrypted2));
```

Output:
```
Encrypted 1: 9157a9881d35236bf8cd736d31cdad28_6bd72fd7fe7006d2977fa37e8453ce769942d8d901b56686e73d1110c25de1c3
Encrypted 2: b5065c49806d7375767b4187a05b6dd7_66cc5153e013879a81eefb171bc5dc4ee21af88a43a7b43ee90eeb483ede2967
Decrypted 1: This is a string
Decrypted 2: This is a string
```



## 2 How to protect passwords
Passwords is a very specific type of information.
Passwords are protected in a such a way that there exists no way to get them back to clear text.
But there exists a way to verify that a clear text password matches a protected password.

For storing passwords use bcrypt.

Bcrypt is much appreciated hash function.
It has been around for years and it has not been broken.
It's  protection against brute force attacks is to be slow.
But the downside is that bcrypt is ...slow.
On an 2017 MacBook Pro (2.6 GHz i7) it took approximately 80 ms to hash a 20 character long password.

```java
String clearText = "my string";
IHash hash = new Bcrypt();
String hashedText1 = hash.hash(clearText);
String hashedText2 = hash.hash(clearText);
System.out.println("Hash1: " + hashedText1);
System.out.println("Hash1 matches: " + hash.matches(clearText, hashedText1));
System.out.println("Hash2: " + hashedText2);
System.out.println("Hash2 matches: " + hash.matches(clearText, hashedText2));
```

```
Hash1: $2a$10$nmSYSMOtwKGAXv7qDxK.3.4p5oUnHvgZejx7mRiLQAHCa9DcOO7lq
Hash1 matches: true
Hash2: $2a$10$nMK0TblmwImoTbQ74X/yFeoFDRoUbI4mIp9/A.N0WiEvVQM8AdJmK
Hash2 matches: true
```

All this library does is a very thin wrapping of the excellent library [jBCrypt](http://www.mindrot.org/projects/jBCrypt/)


## 3 How to protect searchable Personally identifiable information (PII)
For example cell phone numbers, email addresses, names, physical address and so on.
If you know that is not a requirement to make the data searchable not or in the future, then encrypt the data as described in how to protect sensitive data.
PII is important to protect as if you have a data breach your data can be matched with
a previous data leak on for example email address or phone number where the passwords have been
cracked. And people have a tendency to reuse their passwords.


For this use two types of cryptographic techniques:
- AES-256 encryption for protecting the data and getting it back to clear text.
  But as the same clear text encrypted twice generates two different encrypted string we can not use this data for searching.
- A hashing (unsalted HMAC-SHA512) for protecting the data and making it searchable.
  This technique will protect the data, but as the same string hashed twice with this technique are the same and thus the data is searchable


```java
String clearText = "my string";
String key = "0123456789abcdef";
IHash hash = new HmacSha512(key);
String hashedText1 = hash.hash(clearText);
String hashedText2 = hash.hash(clearText);
System.out.println("Hash1: " + hashedText1);
System.out.println("Hash2: " + hashedText2);
```

Output:
```java
Hash1: 5b05cf94fe5e45d0fcdb834693882a9c76dd20db01af5d7d4dc2ec630db4805d11147b2e61f0256be4f8938630a67bfdf1fc9dc027d7da5e0498f0d7fcf398dd
Hash2: 5b05cf94fe5e45d0fcdb834693882a9c76dd20db01af5d7d4dc2ec630db4805d11147b2e61f0256be4f8938630a67bfdf1fc9dc027d7da5e0498f0d7fcf398dd
```


# Version handling
It is necessary to have a way to version handle protection of data to:
- Upgrade ciphers and hash functions as security requirements change.
- Replace keys that might have leaked or suspected of have been cracked.

For this there exists the classes CipherLibrary and HashLibrary.
Strings encrypted or hashed with a cipher or hash library gets a version number as a prefix.

```java
String encryptedString, decryptedString;
CipherLibrary cipherLibrary = CipherLibrary.create()
		.addCipher(1, new Aes256Gcm("G2Ed3Zs6qDAft8Az7268BE9bWbAzXm2q"))
		.addCipher(2, new Aes256Gcm("uhgZxYLKn92w2B2ZFUtT2y98Z9K89MmX"))
		.addCipher(3, new Aes256Gcm("et4CgMZd99zPodK7QnLDGMV643GE94Q8"));
encryptedString = cipherLibrary.encrypt(1, "This is a string");
decryptedString = cipherLibrary.decrypt(encryptedString);
System.out.println("Encrypted: " + encryptedString);
System.out.println("Decrypted: " + decryptedString);
encryptedString = cipherLibrary.encrypt(2, "This is a string");
decryptedString = cipherLibrary.decrypt(encryptedString);
System.out.println("Encrypted: " + encryptedString);
System.out.println("Decrypted: " + decryptedString);
```

Output:
```java
Encrypted: v1_e1db0e1d82b33fded99f88aac9e428f5_cec0e2f168d3aff4f71aca5c0c40636ce442d355bb2bf0cc21cafe05656c0bcf
Decrypted: This is a string
Encrypted: v2_7c0f43a5e37a6d62c20859d0a1ea896b_75dcd2387067d978b2c01b1be566cc1fa12b241af47a8362971c6c66565f3d30
Decrypted: This is a string
```

The cipher library can be used to set up an enum.
This can yield concise readable code.
See sample directory for how this can be done.
```java
String encrypted = MyCipher.MOBILE_NUMBER.encrypt("12345");
System.out.println(encrypted);
System.out.println(MyCipher.decrypt(encrypted));
```

# Keys

#### Generation
For AES and other hashes and ciphers secret keys are used to protect the data.
It is important that these keys are randomly generated keys. Use a tool like 1Password
or a site like [RandomKeygen](https://randomkeygen.com/) to generate a secure key.

#### Storage
The secret key need to be stored in a secure location.
For example the secret keys cannot be with the source code.


# More on Cryptography
#### More on Hashing
For more information on hashing read [this excellent article on CrackStation](https://crackstation.net/hashing-security.htm)

#### How secure is a 256 bit key?
A 256 bit key means that that the key consists of 256 zeros and ones.
This means that the possible number of keys is 2^256.
If the key is a randomly generated key a brute force attack is required.
I.e. the attacker has to test with all possible combinations of zeros and ones.
The average number of guesses needed to guess a 256 bit key is 2^128 (3.4e38).

Lets assume that the attacker has a computer that can test 2 billions (2e9) keys per second.
Say that the attacker connects a billion of these computers.
Then the attacker can test 2,000,000,000,000,000,000 (2e18 = 2e9 * 1e9) keys per seconds.
In a year the attacker can test 6.2e25 keys (2e18 * 30,758,400 seconds in a year).

The average number of years to guess a key would be 5,483,870,967,741 (3.4e38/6.2e25).
I.e. more than 5000 billion years.

The number of keys per second that one can test is debatable, but the at the end of the day you simply cannot brute force crack a 256 bit key.
See similar calculations
[here](https://www.reddit.com/r/theydidthemath/comments/1x50xl/time_and_energy_required_to_bruteforce_a_aes256/)
and
[here](https://bitcoin.stackexchange.com/questions/2847/how-long-would-it-take-a-large-computer-to-crack-a-private-key).

