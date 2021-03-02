import java.util.Arrays;
import java.util.Scanner;
import java.util.*;
import java.util.regex.*;

public class SAES {

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
		SAES saes = new SAES();
		System.out.println(saes.gfMul(3,3));
	}
}

