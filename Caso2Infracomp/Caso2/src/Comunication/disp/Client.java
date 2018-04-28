package Comunication.disp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;



public class Client 
{
	private final  String[] ALGORITHMS = {"AES","RSA","HMACSHA1"};
	
	private final  String PADDING="/ECB/PKCS5Padding";
	
	private Socket socket;
	
	private BufferedReader reader;
	
	private PrintWriter out;
	
	private SecretKey serverKey;
	
	private X509Certificate serverCert;
	
	private X509Certificate clientCert;
	
	private KeyPair pair;

	private Scanner scan;
	

	public Client() throws Exception
	{
		pair = Encryption.createKeyPair();
		clientCert = Encryption.createCertificate(pair);
	}
	
	public boolean sendState(String position) throws Exception
	{	
//		ALGORITHMS[0]=chooseSimetricAlg();
//		ALGORITHMS[2]=chooseHMAC();
		conect();
		init();
		sendAlgorithms();
		sendCert();
		receiveCert();
		receiveKey();
		
		byte[] cipher = Encryption.cipher(position.getBytes(),serverKey, ALGORITHMS[0]+PADDING);
		out.println("ACT1:"+encapsulate(cipher));
		System.out.println("CLT: ACT1:"+encapsulate(cipher));
		
		byte[] mac = Encryption.calcMAC(position.getBytes(), serverKey, ALGORITHMS[2]);
	    out.println("ACT2:"+encapsulate(Encryption.cipher(mac, serverCert.getPublicKey(),ALGORITHMS[1])));
	    System.out.println("CLT: ACT2:"+encapsulate(Encryption.cipher(mac, serverCert.getPublicKey(),ALGORITHMS[1])));
	    String line = reader.readLine();
	    System.out.println("SRV: "+line);
	    System.err.println("fin----------------------------------------------------------------------");
	    return line.equals("ESTADO:OK");
	}
	
	private void conect() throws Exception
	{
		socket = new Socket("172.24.42.41", 8081);
		reader = new BufferedReader( new InputStreamReader( socket.getInputStream( ) ) );
		out = new PrintWriter( socket.getOutputStream( ), true );
		
	}
	
	private boolean init() throws IOException
	{
		out.println("HOLA");
		String resp = reader.readLine();
		System.out.println("CLT: HOLA");
		System.out.println("SRV: "+resp);
		return resp.equals("INICIO");
	}
	
	private boolean sendAlgorithms() throws Exception
	{
		String msg = null;
		for(String s: ALGORITHMS)
			if(msg==null)
				msg="ALGORITMOS:"+s;
			else msg+= ":"+s;
		out.println(msg);
		String resp=  reader.readLine();
		System.out.println("CLT: "+msg);
		System.out.println("SRV: "+resp);
		return resp.equals("ESTADO:OK");
	}
	
	private void sendCert() throws Exception
	{
		out.println("CERTCLNT");
		socket.getOutputStream().write(clientCert.getEncoded());
		socket.getOutputStream().flush();
		System.out.println("CLT: CERTCLNT");
		System.out.println("SRV: "+reader.readLine());
		System.out.println("SRV: "+reader.readLine());
		out.println("ESTADO:OK");
		System.out.println("CLT: ESTADO:OK");
		
	}
	
	private void receiveCert() throws Exception
	{
		byte[] bytes2 = new byte[1000];
		int h = socket.getInputStream().read(bytes2);
		bytes2= Arrays.copyOf(bytes2,h );
//		System.out.print(new String(bytes2));
		serverCert = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(bytes2));
	}
	
	private void receiveKey() throws Exception
	{
		String line = reader.readLine();
		System.out.println("SRV: "+line);
        byte[] data = extract(line.split(":")[1]);
        
	    byte[] key = Encryption.decipher(data , pair.getPrivate(),ALGORITHMS[1]);
	    
		serverKey =  new SecretKeySpec(key, 0, key.length, ALGORITHMS[1]);
	}

	private String encapsulate(byte[] data)
	{
		String rta = "";
		for (byte b: data)
			 rta+= String.format("%2s",Integer.toHexString((char)b & 0xFF)).replace(' ', '0');
		return rta;
	}


	private byte[] extract(String data)
	{
		byte[] rta = new byte[data.length() / 2];
	    for (int i = 0; i < rta.length; i++)
	      rta[i] = Integer.decode("#"+data.substring(i * 2, (i + 1) * 2)).byteValue();
	    return rta;
	}
	
	public static void main(String[] args) {
		try
		{
			Client c = new Client();
			c.sendState("41 24.2028, 2 10.4418");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private String chooseSimetricAlg() throws IOException{

		scan= new Scanner(System.in);
		while(true)
		{
			System.out.println("Ingrese el Numero del algoritmo con cual quiere hacer el cifrado simetrico \n 1.AES \n 2.BLOWFISH");
			String x = scan.nextLine();
			switch (x) {
			case "1":
				return "AES";
			case "2":
				return "BLOWFISH";
			default:
				System.err.println("debe ingresar una de las opciones posibles");
				break;
			}
		}

	}

	private String chooseHMAC() throws IOException{
		while(true)
		{
			System.out.println("Ingrese el Numero del algoritmo con cual quiere hacer el Hash \n 1.HMACMD5 \n 2.HMACSHA1 \n 3.HMACSHA256");
			String x = scan.nextLine();
			switch (x) {
			case "1":
				return "HMACMD5";
			case "2":
				return "HMACSHA1";
			case "3":
				return "HMACSHA256";
			default:
				System.err.println("debe ingresar una de las opciones posibles");
				break;
			}
		}
	}
	
}


