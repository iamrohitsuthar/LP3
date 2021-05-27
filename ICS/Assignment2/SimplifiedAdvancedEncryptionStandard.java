import java.util.Arrays;
import java.util.Scanner;
import java.util.*;
import java.util.regex.*;

public class SimplifiedAdvancedEncryptionStandard {

	private static final String[][] SBOX = { {"1001","0100","1010","1011"},{"1101","0001","1000","0101"},{"0110","0010","0000","0011"},{"1100","1110","1111","0111"} };
	private static String key0 = null, key1 = null, key2 = null;
	private static int encryptionConstantMatrix[][] = { {1, 4}, {4, 1} };

	public SimplifiedAdvancedEncryptionStandard(String key) {
		generateKeys(key);
	}

	private int binaryToDecimal(String binary) {
		return Integer.parseInt(binary, 2);
	}

	private String decimalToBinary(int decimal, int binaryStringSize) {
		return String.format("%" + binaryStringSize + "s", Integer.toBinaryString(decimal & 0xFF)).replace(' ', '0');
	}

	public String stringXOR(String a, String b) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < a.length(); i++) {
    		sb.append(a.charAt(i) ^ b.charAt(i));
		}
		return sb.toString();
	}

	// Galois field Multiplication
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

	private String nibbleSubstitution(String input) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < input.length() / 4 ; i++) {
			String str = input.substring(i*4, (i*4)+4);
			sb.append(SBOX[binaryToDecimal(str.substring(0,2))][binaryToDecimal(str.substring(2,4))]);
		}
		return sb.toString();
	}

	private String shiftRow(String str) {
		// Swap 2nd and 4th nibble
		StringBuilder sb = new StringBuilder();
		sb.append(str.substring(0,4));
		sb.append(str.substring(12, 16));
		sb.append(str.substring(8,12));
		sb.append(str.substring(4,8));
		return sb.toString();
	}

	private String rotateNibble(String word) {
		return word.substring(4,8) + word.substring(0,4);
	}

	private void generateKeys(String key) {
		String w0 = key.substring(0,8);
		String w1 = key.substring(8,16);
		String w2 = stringXOR(stringXOR(w0, "10000000"), nibbleSubstitution(rotateNibble(w1)));
		String w3 = stringXOR(w2, w1);
		String w4 = stringXOR(stringXOR(w2, "00110000"), nibbleSubstitution(rotateNibble(w3)));
		String w5 = stringXOR(w4, w3);
		
		key0 = w0 + w1;
		key1 = w2 + w3;
		key2 = w4 + w5;
	}

	private String getKeys() {
		StringBuilder sb = new StringBuilder();
		sb.append("Key0: "+key0 + "\n");
		sb.append("Key1: "+key1 + "\n");
		sb.append("Key2: "+key2 + "\n");
		return sb.toString();
	}


	public String encrypt(String plainText) {
		// Round 0 - Add Key
		String roundZeroResult = stringXOR(plainText, key0);
		// Round 1 - Nibble Substitution -> Shift Row -> Mix Columns -> Add Key
		String shiftRowResult = shiftRow(nibbleSubstitution(roundZeroResult));
		String matrix[][] = new String[2][2];
		int p = 0;
		for(int i = 0 ; i < 2 ; i++) {
			for(int j = 0 ; j < 2 ; j++) {
				matrix[i][j] = shiftRowResult.substring((p * 4), (p*4)+4);
				p++;
			}
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < encryptionConstantMatrix.length ; i++) {
			for(int j = 0 ; j < matrix.length ; j++) {
				String tempResults[] = new String[2];
				for(int k = 0 ; k < 2 ; k++) {
					tempResults[k] = decimalToBinary(gfMul(encryptionConstantMatrix[i][k],binaryToDecimal(matrix[k][j])), 4);
				}
				sb.append(stringXOR(tempResults[0], tempResults[1]));
			}
		}
		String res = sb.toString();
		String mixColumnsResult = res.substring(0,4) + res.substring(8,12) + res.substring(4,8) + res.substring(12, 16);		
		String roundOneResult = stringXOR(mixColumnsResult, key1);
		// Round 2 - Nibble Substitution -> Shift Row -> Add Key
		String roundTwoResult = stringXOR(shiftRow(nibbleSubstitution(roundOneResult)), key2);
		return roundTwoResult;
	}

	public static void main(String[] args) {
		String key = "0100101011110101";
		String msg = "1101011100101000";
		SimplifiedAdvancedEncryptionStandard simplifiedAdvancedEncryptionStandard = new SimplifiedAdvancedEncryptionStandard(key);
		System.out.println(simplifiedAdvancedEncryptionStandard.getKeys());
		System.out.println("Encrypted Message: "+simplifiedAdvancedEncryptionStandard.encrypt(msg));
	}
}