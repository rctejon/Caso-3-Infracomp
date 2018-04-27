package Comunication.disp;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.crypto.*;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

public class Encryption
{
	public static byte[] decipher(byte [] data, Key llave, String algoritmo) throws Exception
	{ 
		Cipher cipher = Cipher.getInstance(algoritmo); 
		cipher.init(Cipher.DECRYPT_MODE, llave);
		return cipher.doFinal(data); 
	}

	public static byte[] cipher(byte[] data, Key llave, String algoritmo) throws Exception
	{
		System.out.println(algoritmo);
		Cipher cipher = Cipher.getInstance(algoritmo);
		cipher.init(Cipher.ENCRYPT_MODE, llave);
		return cipher.doFinal(data);
	}
	

	public static byte[] calcMAC(byte[] data, Key llave, String algoritmo) throws Exception
	{
	    Mac mac = Mac.getInstance(algoritmo);
	    mac.init(llave);
	    return mac.doFinal(data);
	}
	
	public static KeyPair createKeyPair() throws Exception
	{
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	    keyGen.initialize(1024, new SecureRandom());
	    return keyGen.generateKeyPair();
	}
	
	public static X509Certificate createCertificate(KeyPair pair) throws Exception
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(System.currentTimeMillis()- 31535000000L); 
		Date startDate = gc.getTime();
		System.out.println(startDate);
		gc.add(GregorianCalendar.YEAR, 4);			 
		Date expiryDate = gc.getTime();              
		BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());       
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		X500Principal subjectName = new X500Principal("CN=Test V3 Certificate");
		 
		certGen.setSerialNumber(serialNumber);
		certGen.setIssuerDN(new X500Principal("CN=Test Certificate"));
		certGen.setNotBefore(startDate);
		certGen.setNotAfter(expiryDate);
		certGen.setSubjectDN(subjectName);
		certGen.setPublicKey(pair.getPublic());
		certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
		
		
		Security.addProvider(new BouncyCastleProvider());

		return  certGen.generate(createKeyPair().getPrivate(), "BC");  
	}
	
	public static Key createKey(String algoritmo) throws NoSuchAlgorithmException
	{
		KeyGenerator keygen = KeyGenerator.getInstance(algoritmo);
		return keygen.generateKey();
	}
}
