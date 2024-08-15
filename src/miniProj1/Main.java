package miniProj1;

import java.util.Scanner;

import Service.MemberService;

public class Main {
	
	private static Scanner sc;
	private static MemberService memberService = new MemberService();
	private static long loginId = -1L;
	
	public static void main(String[] args) {
		while(true) {
			if (loginId == -1L) {
				mainMenu();
			}else {
				userMenu();
			}
		}
	}
	
	private static void mainMenu() {
		System.out.println("======================");
		System.out.println("1. 회원가입");
		System.out.println("2. 로그인");
		System.out.println("3. 종료");
		System.out.print("원하는 기능의 번호를 입력해주세요. : ");
		sc = new Scanner(System.in);
		int x = sc.nextInt();
		switch(x) {
			// 회원가입
			case 1 -> register();
			// 로그인
			case 2 -> login();
			case 3 -> {
				System.out.println("시스템을 종료합니다.");
				System.exit(0);
			}
		}
		
	}
	
	private static void login() {
		sc = new Scanner(System.in);
		System.out.print("아이디 : ");
		String username = sc.nextLine();
		System.out.println("비밀번호 : ");
		String password = sc.nextLine();
		
		if (memberService.login(username, password)) {
			loginId = memberService.getId(username);
			System.out.println("로그인 성공");
			return;
		}
		System.out.println("로그인 실패");
	}

	private static void register() {
		sc = new Scanner(System.in);
		System.out.println("회원 가입 화면");
		System.out.println("=====================");
		System.out.print("아이디 : ");
		String username = sc.nextLine();
		
		System.out.print("비밀번호 : ");
		String password = sc.nextLine();
		
		System.out.print("이름 : ");
		String name = sc.nextLine();
		
		System.out.print("전화번호 : ");
		String phone = sc.nextLine();
		
		System.out.print("주소 : ");
		String address = sc.nextLine();
		
		System.out.print("성별 (남/여):");
		String gender = sc.nextLine();
		memberService.registerMember(username, password, name, phone, address, gender);
	}
	
	private static void userMenu() {
		
	}
	
	


}
