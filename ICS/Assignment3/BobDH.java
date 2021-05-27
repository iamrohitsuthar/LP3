import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class BobDH
{
	private static Socket soc;
	private static Scanner sc;

	public static void main(String[] args) throws UnknownHostException, IOException
	{
		int P,G;
		int b;
		
		soc = new Socket("127.0.0.1",8088);
		System.out.println("Connected to "+soc.getRemoteSocketAddress());
		DataInputStream dis = new DataInputStream(soc.getInputStream());
		DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
		sc = new Scanner(System.in);
		
		System.out.print("Enter Modulus (P) Prime Number: ");
		P = sc.nextInt();
		System.out.print("Enter Base (G) Primitive Root of P: ");
		G = sc.nextInt();
		
		dos.writeInt(P);
		dos.writeInt(G);
		
		System.out.print("Enter Private key : ");
		b = sc.nextInt();
		
		//y = (G ^ b) mod P
		BigInteger y = (BigInteger.valueOf(G).pow(b)).mod(BigInteger.valueOf(P));
		System.out.println("Bob's Public key(y)= " + y.intValue());
		dos.writeInt(y.intValue());
		
		//kb = (x ^ b) mod P
		int x = dis.readInt();
		System.out.println("Alice's Public key(Ya) = " + x);
		BigInteger kb = (BigInteger.valueOf(x).pow(b)).mod(BigInteger.valueOf(P));
		
		System.out.println("Shared Key: " + kb.intValue());
		soc.close();
	}
}
