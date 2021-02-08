package _3D;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		Random random = new Random();

		float r = random.nextFloat();
		// System.out.println(r);
		// System.out.println(r - 0.5f);
		// System.out.println((random.nextFloat() - 0.5) * 3);
		System.out.println((random.nextFloat() - 0.5f) * 100f);

	}

}
