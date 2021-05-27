import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Random;

public class RSAClient
{
	
	public static int getPublicKey(int phi)
	{
		Random r = new Random();
		int result = r.nextInt(phi-2) + 2;
		while (BigInteger.valueOf(phi).gcd(BigInteger.valueOf(result)).intValue() != 1) {
			result = r.nextInt(phi-2) + 2;
		}
		return result;
	}
	
	public static int getPrivateKey(int phi, int e)
	{
		int k = 1;
		double d = (double) (k * phi + 1) / (double) e;
		while (Math.floor(d) != d)
		{
			k = k + 1;
			d = (double) (k * phi + 1) / (double) e;
		}
		return (int)d;
	}	

	public static void main(String[] args) throws UnknownHostException, IOException
	{		
		Socket soc = new Socket("127.0.0.1",8088);
		System.out.println("Connected to "+soc.getRemoteSocketAddress());
		DataInputStream dis = new DataInputStream(soc.getInputStream());
		DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter first prime number (P): ");
		int p = sc.nextInt();
		System.out.print("Enter second prime number (Q): ");
		int q = sc.nextInt();

		int n = p * q;
		System.out.println("n: " + n);

		int phi = (p - 1) * (q - 1);
		System.out.println("Ø(n): " + phi);
		
		int e = getPublicKey(phi);
		int d = getPrivateKey(phi, e);
		
		dos.writeInt(n);
		dos.writeInt(e);
		
		System.out.println("Public key {e,n} = { " + e + ", " + n + " }");
		System.out.println("Private key {d,n} = { " + d + ", " + n + " }");

		System.out.println("Waiting for Ciphertext....");
		int cipherText = dis.readInt();
		System.out.println("Received Ciphertext : "+cipherText);
		BigInteger plainText = BigInteger.valueOf(cipherText).pow(d).mod(BigInteger.valueOf(n));

		System.out.println("Decrypted Plain Text: "+plainText);
		
		soc.close();
		sc.close();		
		
	}
}