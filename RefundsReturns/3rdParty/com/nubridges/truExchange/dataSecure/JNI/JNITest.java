package com.nubridges.truExchange.dataSecure.JNI;

import java.io.UnsupportedEncodingException;
import com.nubridges.nbutilities.NuBridgesException;

public class JNITest {

	public static void main(String args[])
	{
		try {
			JNITest theTest = new JNITest();
			theTest.doit(args);
		}
		catch (NuBridgesException e) {
			System.out.println(e.GetExtendedMessage());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void doit(String args[]) throws NuBridgesException, UnsupportedEncodingException
	{
		if (args.length < 1) {
			throw new NuBridgesException("Invalid parms: specify resource");
		}
        System.out.println("java.library.path="+System.getProperty("java.library.path"));                       
		NuBridgesCipher nbds = new NuBridgesCipher("", "", args[0], "");
		byte[] data = "8787555587875555    ".getBytes("1047");
//		byte[] cipher = nbds.ShadowEncryptByteArray(data);
        byte[] cipher = nbds.EncryptByteArray(data);
		System.out.print("Cipher (len " + cipher.length + "):\n" + new String(cipher, "1047") + "\n");
//        byte[] plain = nbds.ShadowDecryptByteArray(cipher);
        byte[] plain = nbds.DecryptByteArray(cipher);
		System.out.print(" Plain (len " + plain.length + "):\n" + new String(plain, "1047") + "\n");
	}
}
