package com.lazyproductions.appserver.database;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
	private static byte[] key = new byte[16];
	private static SecretKeySpec secretKey;
	private static Cipher cipher;
	private static final Encryption INSTANCE = new Encryption();
	
	private Encryption() {
		secretKey = new SecretKeySpec(key, "AES");
		
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
		
	public static Encryption getInstance() {
		return INSTANCE;
	}
	
	public String encrypt(String stringToEncrypt) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		return Base64.getEncoder().encodeToString(cipher.doFinal(stringToEncrypt.getBytes("UTF-8")));
		
	}
}
