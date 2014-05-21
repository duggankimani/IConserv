package com.wira.pmgt.server.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SecurityHelper {

	
	static{
		// "BC" is the name of the BouncyCastle provider
		Security.addProvider(new BouncyCastleProvider());
		
	}
	
	public static String encrypt(String password){
		
		try{
			
			KeyGenerator keyGen = KeyGenerator.getInstance("DES", "BC");
			keyGen.init(new SecureRandom());
			/*
			 * This will generate a random key, and encrypt the data
			 */
			Key key = keyGen.generateKey();
			
			//Cipher
			Cipher encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding", "BC");
			encrypt.init(Cipher.ENCRYPT_MODE, key);
	
			byte[] encryptedData = encrypt.doFinal(password.getBytes());
					
			return new String(encryptedData);
		
		}catch(NoSuchAlgorithmException |
				NoSuchProviderException | NoSuchPaddingException | InvalidKeyException |
				IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	public static void main(String[] args) {
		System.err.println(encrypt("pass"));
	}
	
}
