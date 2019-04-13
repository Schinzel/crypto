# crypto
The purpose of this library is to offer protection of data from prying eyes through encryption and hash functions

[![Build Status](https://travis-ci.org/Schinzel/crypto.svg?branch=master)](https://travis-ci.org/Schinzel/crypto)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=coverage)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=security_rating)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=sqale_index)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Schinzel_crypto&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=Schinzel_crypto)
#### Sample code
```java
Aes256Gcm("0123456789abcdef0123456789abcdef");
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
		<url>http://maven-repo.schinzel.io/release</url>
	</repository>
</repositories>    

<dependencies>
	<dependency>
		<groupId>io.schinzel</groupId>
		<artifactId>crypto</artifactId>
		<version>1.XX</version>
	</dependency>
</dependencies>    
```
