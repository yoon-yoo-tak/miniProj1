package util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class IOUtil {
	public static int getInput(Scanner sc) {
		try {
			return sc.nextInt();
		}catch (InputMismatchException e) {
			sc.nextLine();
			System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
			return -1;
		}
	}
	
	public static String getString(Scanner sc) {
		String ret = sc.next();
		sc.nextLine();
		return ret;
	}
}
