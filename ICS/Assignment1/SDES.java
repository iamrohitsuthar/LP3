import java.util.Arrays;
import java.util.Scanner;

public class SDES {
	
	private static final int[] P10 = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
	private static final int P10_MAX = 10;
	private static final int[] P8 = { 6, 3, 7, 4, 8, 5, 10, 9 };
	private static final int P8_MAX = 8;
	private static int[] key1 = null;
	private static int[] key2 = null;
	
	public int[] permute(int key[], int type) {
		int res[] = new int[type];
		for(int i = 0 ; i < type ; i++) {
			if(type == P10_MAX)
				res[i] = key[P10[i]-1];
			else
				res[i] = key[P8[i]-1];
		}
		return res;
	}
	
	public void shiftByOne(int key[]) {
		int temp = key[0];
		for(int i = 0 ; i < key.length-1 ; i++) {
			key[i] = key[i+1];
		}
		key[key.length-1] = temp;
	}
	
	public void shift(int[] key, int shiftSize) {
		for(int i = 0 ; i < shiftSize ; i++) {
			shiftByOne(key);
		}
	}
	
	public void generateKeys(String inputKey) {
		int[] key = new int[inputKey.length()];
		for(int i = 0 ; i < inputKey.length() ; i++) {
			key[i] = Integer.parseInt(String.valueOf(inputKey.charAt(i)));
		}
		key = permute(key, P10_MAX);
		int l[] = new int[5];
		int r[] = new int[5];
		System.arraycopy(key, 0, l, 0, 5);
		System.arraycopy(key, 5, r, 0, 5);
		shift(l, 1);
		shift(r, 1);
		System.arraycopy(l, 0, key, 0, 5);
		System.arraycopy(r, 0, key, 5, 5);
		key1 = permute(key, P8_MAX);
		shift(l, 2);
		shift(r, 2);
		System.arraycopy(l, 0, key, 0, 5);
		System.arraycopy(r, 0, key, 5, 5);
		key2 = permute(key, P8_MAX);
		System.out.println("Key1: "+Arrays.toString(key1));
		System.out.println("Key2: "+Arrays.toString(key2));
	}
	
	
	public static void main(String[] args) {
		SDES sdes = new SDES();
		String key = null;
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter 10-bit key: ");
		key = sc.next();
		sdes.generateKeys(key);
	}
}
