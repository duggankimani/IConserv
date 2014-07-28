package com.duggan.workflow.test.encryp;

import com.wira.pmgt.server.util.CryptoUtils;

public class UserPassEncryption {

	public static void main(String[] args) {
		String plainPass = "pass";
		
		String encryptedPassword = CryptoUtils.getInstance().encryptPassword(plainPass);
		
		System.err.println(encryptedPassword);
		
		System.err.println(CryptoUtils.getInstance().checkPassword(plainPass, encryptedPassword));
		System.err.println( CryptoUtils.getInstance().checkPassword(plainPass, "cMp460N4Uxb3mQHKE9YQCFNoKcO+ARQn"));
	}
}
