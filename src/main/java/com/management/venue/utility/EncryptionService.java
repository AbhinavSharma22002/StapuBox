package com.management.venue.utility;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

	@Value("${stapu.encryption.secret-key:default_secret_16}")
	private String secretKey;

	@Value("${stapu.encryption.algorithm:AES}")
	private String algorithm;

	public String encode(String data) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] encrypted = cipher.doFinal(data.getBytes());
			return Base64.getUrlEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			throw new RuntimeException("Error while encoding ID", e);
		}
	}

	public String decode(String encodedData) {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedData);
			byte[] original = cipher.doFinal(decodedBytes);
			return new String(original);
		} catch (Exception e) {
			throw new RuntimeException("Error while decoding ID - Invalid or tampered key", e);
		}
	}
}