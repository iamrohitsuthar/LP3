import java.util.Arrays;
import java.util.Scanner;

public class SDES {
	
	private static final int[] P10 = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
	private static final int[] P8 = { 6, 3, 7, 4, 8, 5, 10, 9 };
	private static final int[] IP = { 2, 6, 3, 1, 4, 8, 5, 7 };	
	private static final int[] IP_INV = { 4, 1, 3, 5, 7, 2, 8, 6};	
	private static final int[] EP = { 4, 1, 2, 3, 2, 3, 4, 1 };
	private static int[] key1 = null;
	private static int[] key2 = null;
	
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
	
	public void generateKeys(String inputKey) {
		int[] key = new int[inputKey.length()];
		for(int i = 0 ; i < inputKey.length() ; i++) {
			key[i] = Integer.parseInt(String.valueOf(inputKey.charAt(i)));
		}
		key = permute(key, P10);
		int l[] = new int[5];
		int r[] = new int[5];
		System.arraycopy(key, 0, l, 0, 5);
		System.arraycopy(key, 5, r, 0, 5);
		shift(l, 1);
		shift(r, 1);
		System.arraycopy(l, 0, key, 0, 5);
		System.arraycopy(r, 0, key, 5, 5);
		key1 = permute(key, P8);
		shift(l, 2);
		shift(r, 2);
		System.arraycopy(l, 0, key, 0, 5);
		System.arraycopy(r, 0, key, 5, 5);
		key2 = permute(key, P8);
		System.out.println("Key1: "+Arrays.toString(key1));
		System.out.println("Key2: "+Arrays.toString(key2));
	}
	
	public void encrypt(String input) {
		int[] text = new int[8];
		for(int i = 0 ; i < 8 ; i++) {
			text[i] = Integer.parseInt(String.valueOf(input.charAt(i)));
		}
		text = permute(text, IP);
		System.out.println("Initial Permutation: "+Arrays.toString(text));

	}
	
	public static void main(String[] args) {
		SDES sdes = new SDES();
		String key = null;
		String text = null;
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter 10-bit key: ");
		key = sc.next();
		sdes.generateKeys(key);
		System.out.print("Enter plain text for encryption: ");
		text = sc.nextLine() + sc.nextLine();
		sdes.encrypt(text);
	}
}
