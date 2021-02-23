import java.util.Arrays;
import java.util.Scanner;
import java.util.*;
import java.util.regex.*;

public class SDES {
	
	private static final int[] P10 = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
	private static final int[] P8 = { 6, 3, 7, 4, 8, 5, 10, 9 };
	private static final int[] IP = { 2, 6, 3, 1, 4, 8, 5, 7 };	
	private static final int[] IP_INV = { 4, 1, 3, 5, 7, 2, 8, 6};	
	private static final int[] EP = { 4, 1, 2, 3, 2, 3, 4, 1 };
	private static final int[] P4 = { 2,4,3,1 };
	private static final int[][] SBOX0 = { { 1,0,3,2 }, { 3,2,1,0 }, { 0,2,1,3 }, { 3,1,3,2 } };
	private static final int[][] SBOX1 = { { 0,1,2,3 }, { 2,0,1,3 }, { 3,0,1,0 }, { 2,1,0,3 } };

	private static int[] key1 = null;
	private static int[] key2 = null;

	public SDES(String key) {
		generateKeys(key);
	}
	
	private int[] permute(int key[], int type[]) {
		int res[] = new int[type.length];
		for(int i = 0 ; i < type.length ; i++) {
			res[i] = key[type[i]-1];
		}
		return res;
	}
	
	private void shiftByOne(int key[]) {
		int temp = key[0];
		for(int i = 0 ; i < key.length-1 ; i++) {
			key[i] = key[i+1];
		}
		key[key.length-1] = temp;
	}
	
	private void shift(int[] key, int shiftSize) {
		for(int i = 0 ; i < shiftSize ; i++) {
			shiftByOne(key);
		}
	}
	
	private void generateKeys(String inputKey) {
		//converting string key into int array
		int[] key = new int[inputKey.length()];
		for(int i = 0 ; i < inputKey.length() ; i++) {
			key[i] = Integer.parseInt(String.valueOf(inputKey.charAt(i)));
		}
		//p10 permute
		key = permute(key, P10);
		
		//dividing the key into two halves - left and right half
		int leftHalf[] = new int[5];
		int rightHalf[] = new int[5];
		System.arraycopy(key, 0, leftHalf, 0, 5);
		System.arraycopy(key, 5, rightHalf, 0, 5);

		//shifting the left and right halfs by 1
		shift(leftHalf, 1);
		shift(rightHalf, 1);

		//merging the left and right halfs
		System.arraycopy(leftHalf, 0, key, 0, 5);
		System.arraycopy(rightHalf, 0, key, 5, 5);

		//p8 permute and store the result into key1
		key1 = permute(key, P8);

		//shift the left and right half by 2
		shift(leftHalf, 2);
		shift(rightHalf, 2);

		System.arraycopy(leftHalf, 0, key, 0, 5);
		System.arraycopy(rightHalf, 0, key, 5, 5);

		//p8 permute and store the result into key2
		key2 = permute(key, P8);

		//generated keys
		System.out.println("Key1: "+Arrays.toString(key1));
		System.out.println("Key2: "+Arrays.toString(key2));
	}

	private int binToDec(int a, int b) {
		if(a == 0 && b == 0) return 0;
		else if(a == 0 && b == 1) return 1;
		else if(a == 1 && b == 0) return 2;
		else return 3;
	}

	private int[] DecToBin(int num) {
		if(num == 0) return new int[] {0,0};
		else if(num == 1) return new int[] {0,1};
		else if(num == 2) return new int[] {1,0};
		else return new int[] {1,1};
	}

	private int[] getSBoxResult(int subBlock[], int[][] sbox) {
		int rowNo = binToDec(subBlock[0],subBlock[3]);
		int columnNo = binToDec(subBlock[1],subBlock[2]);
		return DecToBin(sbox[rowNo][columnNo]);
	}
	
	private int[] fk(int[] rBlock, int key[], int[] lBlock) {
		//expand and permute the right blocks
		int expandedSubBlock[] = permute(rBlock, EP);
		System.out.println("EP res: "+Arrays.toString(expandedSubBlock));

		//XOR the EP res with key1
		for(int i = 0 ; i < expandedSubBlock.length ; i++) {
			expandedSubBlock[i] = expandedSubBlock[i] ^ key[i];
		}
		System.out.println("Res after XOR with key: "+Arrays.toString(expandedSubBlock));
		
		int left[] = new int[4];
		int right[] = new int[4];
		System.arraycopy(expandedSubBlock, 0, left, 0, 4);
		System.arraycopy(expandedSubBlock, 4, right, 0, 4);

		//pass left block to SBOX0
		int sBox0Res[] = getSBoxResult(left, SBOX0);
		System.out.println("SBox0 res: "+Arrays.toString(sBox0Res));
		
		//pass right block to SBOX1
		int sBox1Res[] = getSBoxResult(right, SBOX1);
		System.out.println("SBox1 res: "+Arrays.toString(sBox1Res));

		int combineSBoxesRes[] = new int[4];
		System.arraycopy(sBox0Res, 0, combineSBoxesRes, 0, 2);
		System.arraycopy(sBox1Res, 0, combineSBoxesRes, 2, 2);
		System.out.println("SBoxes combine res: "+Arrays.toString(combineSBoxesRes));

		//p4 permutation
		int P4PermRes[] =  permute(combineSBoxesRes, P4);
		System.out.println("Result after P4 Permutation: "+Arrays.toString(P4PermRes));

		//XOR P4 Perm result with left block
		for(int i = 0 ; i < lBlock.length ; i++) {
			P4PermRes[i] = P4PermRes[i] ^ lBlock[i];
		}

		System.out.println("Result after XOR with left half: "+Arrays.toString(P4PermRes));
		return P4PermRes;
	}

	private String convertStringToBinary(String input) {
        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar))
                            .replaceAll(" ", "0")                      
            );
        }
        return result.toString();
    }

	private String convertBinaryToString(String input) {
		StringBuilder sb = new StringBuilder();
		Pattern p = Pattern.compile("[\\w ]{0,8}");
        Matcher m = p.matcher(input);
        while (m.find()) {
			if(!m.group().isEmpty())
            	sb.append(new Character((char)Integer.parseInt(m.group(), 2)).toString());
        }
		return sb.toString();
	}

    private List<String> convertBinaryIntoBlocks(String binary, int blockSize, String separator) {

        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < binary.length()) {
            result.add(binary.substring(index, Math.min(index + blockSize, binary.length())));
            index += blockSize;
        }

        return result;
    }

	public String encrypt(String input, String type) {
		//type - BINARY FORM | TEXT MESSAGE FORM
		List<String> blocks;
		StringBuilder builder = new StringBuilder();

		if(type.equals("TEXT_MESSAGE_FORM")) {
			blocks = convertBinaryIntoBlocks(convertStringToBinary(input), 8, " ");
		}
		else {
			blocks = new ArrayList<>();
			blocks.add(input);
		}
		
		for(int i = 0 ; i < blocks.size() ; i++) {
			String block = blocks.get(i);
			//create 8bit msg block 
			int[] msgBlock = new int[8];
			for(int j = 0 ; j < 8 ; j++) {
				msgBlock[j] = Integer.parseInt(String.valueOf(block.charAt(j)));
			}

			//initial permutation
			msgBlock = permute(msgBlock, IP);
			System.out.println("Initial Permutation Res: "+Arrays.toString(msgBlock));

			//divide 8bit msgblock into left and right halfs
			int leftHalf[] = new int[4];
			int rightHalf[] = new int[4];
			System.arraycopy(msgBlock, 0, leftHalf, 0, 4);
			System.arraycopy(msgBlock, 4, rightHalf, 0, 4);
			System.out.println("left block: "+Arrays.toString(leftHalf));
			System.out.println("right block: "+Arrays.toString(rightHalf));

			System.out.println("\n***** First Round - fk1 *****");
			//first round - fk function with key1
			int fk1Res[] = fk(rightHalf, key1, leftHalf);
			System.out.println("fk1 Result: "+Arrays.toString(fk1Res));
			
			//swap function
			leftHalf = rightHalf;
			rightHalf = fk1Res;

			System.out.println("\n***** Second Round - fk2 *****");
			//second round - fk function with key2
			int fk2Res[] = fk(fk1Res,key2,leftHalf);
			System.out.println("fk2 Result: "+Arrays.toString(fk2Res));

			int res[] = new int[8];
			System.arraycopy(fk2Res, 0, res, 0, 4);
			System.arraycopy(rightHalf, 0, res, 4, 4);
			System.out.println("\nResult before IP inverse: "+Arrays.toString(res));

			//inverse initial permutation
			int encrypedRes[] = permute(res, IP_INV);
			
			for(int ele: encrypedRes) {
				builder.append(ele);
			}
		}

		//return the encrypted message
		return builder.toString();
	}

	public String decrypt(String encryptedInput, String type) {
		StringBuilder builder = new StringBuilder();
		List<String> blocks = convertBinaryIntoBlocks(encryptedInput, 8, " ");
		//System.out.println("blocks:"+blocks.toString());

		for(int i = 0 ; i < blocks.size() ; i++) {
			String block = blocks.get(i);
			//create 8bit msg block 
			int[] msgBlock = new int[8];
			for(int j = 0 ; j < 8 ; j++) {
				msgBlock[j] = Integer.parseInt(String.valueOf(block.charAt(j)));
			}
			//System.out.println("Msg block: "+Arrays.toString(msgBlock));

			//initial permutation
			msgBlock = permute(msgBlock, IP);
			System.out.println("Initial Permutation Res: "+Arrays.toString(msgBlock));

			//divide 8bit msgblock into left and right halfs
			int leftHalf[] = new int[4];
			int rightHalf[] = new int[4];
			System.arraycopy(msgBlock, 0, leftHalf, 0, 4);
			System.arraycopy(msgBlock, 4, rightHalf, 0, 4);
			System.out.println("left block: "+Arrays.toString(leftHalf));
			System.out.println("right block: "+Arrays.toString(rightHalf));

			System.out.println("\n***** First Round - fk1 *****");
			//first round - fk function with key1
			int fk1Res[] = fk(rightHalf, key2, leftHalf);
			System.out.println("fk1 Result: "+Arrays.toString(fk1Res));
			
			//swap function
			leftHalf = rightHalf;
			rightHalf = fk1Res;

			System.out.println("\n***** Second Round - fk2 *****");
			//second round - fk function with key2
			int fk2Res[] = fk(fk1Res,key1,leftHalf);
			System.out.println("fk2 Result: "+Arrays.toString(fk2Res));

			int res[] = new int[8];
			System.arraycopy(fk2Res, 0, res, 0, 4);
			System.arraycopy(rightHalf, 0, res, 4, 4);
			System.out.println("\nResult before IP inverse: "+Arrays.toString(res));

			//inverse initial permutation
			int decryptedRes[] = permute(res, IP_INV);

			for(int ele: decryptedRes) {
				builder.append(ele);
			}
		}
		//return the decrypted message
		if(type.equals("TEXT_MESSAGE_FORM"))
			return convertBinaryToString(builder.toString());
		else
			return builder.toString();
	}
	
	public static void main(String[] args) {
		String key = null, msg = null, type = null;
		int typeInput = -1;
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter 10-bit key: ");
		key = sc.next();
		
		System.out.print("Enter plain text for encryption: ");
		msg = sc.nextLine() + sc.nextLine();

		System.out.println("Text message is in which form? ");
		System.out.println("1 - For binary form");
		System.out.println("2 - For Text message form");
		typeInput = sc.nextInt();
		if(typeInput == 1) type = "BINARY_FORM";
		else if(typeInput == 2)type = "TEXT_MESSAGE_FORM";

		SDES sdes = new SDES(key);

		System.out.println("\n***** ENCRYPTION *****");
		String encryptedMsg = sdes.encrypt(msg, type);
		System.out.println("Encrypted Message: "+encryptedMsg);

		System.out.println("\n***** DECRYPTION *****");
		String decryptedMsg = sdes.decrypt(encryptedMsg, type);
		System.out.println("Decrypted Message: "+decryptedMsg);
	}
}