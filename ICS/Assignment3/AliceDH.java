import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class AliceDH
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket server = new ServerSocket(8088);
		System.out.println("Waiting for connection on port " + server.getLocalPort());
		Socket soc = server.accept();
		System.out.println("Accepted connection from " + soc.getRemoteSocketAddress());
		DataInputStream dis = new DataInputStream(soc.getInputStream());
		DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
		
		int P = dis.readInt();
		int G = dis.readInt();
		int y = dis.readInt();
		System.out.println("P = " + P + " \nG = " + G + "\nBob's Public key(y) = " + y);
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter Private key : ");
		int a = sc.nextInt();
		
		//x = (G ^ a) mod P
		BigInteger x = (BigInteger.valueOf(G).pow(a)).mod(BigInteger.valueOf(P));
		System.out.println("Alice's Public key(Ya) = " + x);
		dos.writeInt(x.intValue());
		
		//ka = (y ^ a) mod P
		BigInteger ka=(BigInteger.valueOf(y).pow(a)).mod(BigInteger.valueOf(P));
		System.out.println("Shared Key : "+ ka.intValue());
	}
}