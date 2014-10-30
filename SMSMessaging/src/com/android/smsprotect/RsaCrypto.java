package com.android.smsprotect;


import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import android.util.Base64;
//import android.util.Base64;



	




public class RsaCrypto {
	
	
	private static Cipher cipher;
	public static PrivateKey privKey;
	public static PublicKey pubKey;
	private KeyFactory fact;
	private RSAPublicKeySpec pub;
	private RSAPrivateKeySpec priv;
	public int enc_len;
	private static SecureRandom sr;
	
public RsaCrypto() throws Exception {
	
	//Security.addProvider(new Provider())
	
	 sr = new SecureRandom();
	
	 
	
	KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
	
	 cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

	kpg.initialize(1024, sr);
	KeyPair keyPair = kpg.generateKeyPair();
    privKey = keyPair.getPrivate();
	 pubKey = keyPair.getPublic();
	
	
	//Save key to file
	
	 fact = KeyFactory.getInstance("RSA");
	 pub = fact.getKeySpec(keyPair.getPublic(),
	  RSAPublicKeySpec.class);
	 priv = fact.getKeySpec(keyPair.getPrivate(),
	  RSAPrivateKeySpec.class);
}
	
		public static PrivateKey getPrivateKey(){

			return privKey;
		}
		
		public static PublicKey getPublicKey(){

			return pubKey;
		}
		
	    @SuppressWarnings("resource")
		public static  String Encrypt(String stringInput) throws Exception {

	    
		// Encrypt
		//create byte array to store string
		byte [] input;
		
		input = stringInput.getBytes("UTF-8");
		
		//intialize cipher for encryption
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		/*
		byte[] encrypted= new byte[cipher.getOutputSize(input.length)];
	     enc_len = cipher.update(input, 0, input.length, encrypted, 0);
		enc_len += cipher.doFinal(encrypted, enc_len);
		
		*/
		
		
		
		
		
		
		
		//return encrypted.toString();
		
		byte [] encryptByte = cipher.doFinal(input);
		
		
		
		return  Base64.encodeToString(encryptByte, Base64.DEFAULT);    //DatatypeConverter.printBase64Binary(encryptByte);   
		
		
		
		//return new String(encryptByte, "UTF-8");
		
		
	    }
	    
	    public static String Decrypt(String encryptedString, PrivateKey privK) throws InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
	    	
	    	cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    	
	    	
	    	
	    	
	    	cipher.init(Cipher.DECRYPT_MODE, privK);
	    	
	        byte[] encryptedMessageByte =  Base64.decode(encryptedString, Base64.DEFAULT);   //DatatypeConverter.parseBase64Binary(encryptedString);
	    	
	    	byte[] decrypted = cipher.doFinal(encryptedMessageByte);
	    	
	    	/*
	    	byte[] decrypted = new byte[cipher.getOutputSize(enc_len)];
	    	int dec_len = cipher.update(encryptByte, 0, enc_len, decrypted, 0);
	    	dec_len += cipher.doFinal(decrypted, dec_len);
	    	
	    	*/
	    	
	    	
	    	
	    	//return decrypted.toString();
	    	
	    	//return new String(decrypted);
	    	
	    	return new String(decrypted);
	    }
	    
	    
	    
}
