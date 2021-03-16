import java.util.Arrays;
import java.util.Scanner;
import java.util.*;
import java.util.regex.*;

public class SAES {

	private static final String[][] SBOX = { {"1001","0100","1010","1011"},{"1101","0001","1000","0101"},{"0110","0010","0000","0011"},{"1100","1110","1111","0111"} };
	private static String key0 = null, key1 = null, key2 = null;

	public SAES(String key) {
		generateKeys(key);
	}

	private String rotateNibble(int word) {
		String str = String.format("%8s", Integer.toBinaryString(word & 0xFF)).replace(' ', '0');
		return str.substring(4,8) + str.substring(0,4);
	}

	private int binToDec(String binary) {
		if(binary.equals("00")) return 0;
		else if(binary.equals("01")) return 1;
		else if(binary.equals("10")) return 2;
		else return 3;
	}

	private String subNibble(String word) {
		String s1 = word.substring(0,4);
		String s2 = word.substring(4,8);
		return SBOX[binToDec(s1.substring(0,2))][binToDec(s1.substring(2,4))] + SBOX[binToDec(s2.substring(0,2))][binToDec(s2.substring(2,4))];	
	}

	private void generateKeys(String key) {
		int w0 = Integer.parseInt(key.substring(0,8),2);
		int w1 = Integer.parseInt(key.substring(8,16),2);
		int w2 = w0 ^ 0b10000000 ^ Integer.parseInt(subNibble(rotateNibble(w1)),2);
		int w3 = w2 ^ w1;
		int w4 = w2 ^ 0b00110000 ^ Integer.parseInt(subNibble(rotateNibble(w3)),2);
		int w5 = w4 ^ w3;

		key0 = String.format("%8s", Integer.toBinaryString(w0 & 0xFF)).replace(' ', '0') + String.format("%8s", Integer.toBinaryString(w1 & 0xFF)).replace(' ', '0');
		key1 = String.format("%8s", Integer.toBinaryString(w2 & 0xFF)).replace(' ', '0') + String.format("%8s", Integer.toBinaryString(w3 & 0xFF)).replace(' ', '0');
		key2 = String.format("%8s", Integer.toBinaryString(w4 & 0xFF)).replace(' ', '0') + String.format("%8s", Integer.toBinaryString(w5 & 0xFF)).replace(' ', '0');
	}

	private String getKeys() {
		StringBuilder sb = new StringBuilder();
		sb.append("Key0: "+key0 + "\n");
		sb.append("Key1: "+key1 + "\n");
		sb.append("Key2: "+key2 + "\n");
		return sb.toString();
	}

	private int gfMul(int a, int b) {
        int product = 0; //the product of the multiplication
            
        while (b > 0) {
            if ((b & 1) != 0) //if b is odd then add the first num i.e a into product result
                product = product ^ a;
            
            a = a << 1; //double first num

            //if a overflows beyond 4th bit
            if ((a & (1 << 4)) != 0)
                a = a ^ 0b10011; // XOR with irreducible polynomial with high term eliminated
            
            b = b >> 1; //reduce second num
        }
        return product;
    }




	public static void main(String[] args) {
		String key = "0100101011110101";
		String msg = "1101011100101000";
		SAES saes = new SAES(key);
		System.out.println(saes.getKeys());
		// System.out.println(saes.gfMul(3,3));
	}
}

