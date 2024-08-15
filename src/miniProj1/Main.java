package miniProj1;

import java.sql.SQLException;
import java.util.Scanner;

import Service.MemberService;

public class Main {
	
	private static Scanner sc;
	private static MemberService memberService = new MemberService();
	private static int loginId = -1;
	
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
		
		System.out.println("1. 로그인");
		System.out.println("2. 다시 입력");
		System.out.println("3. 이전 화면으로");
		
		System.out.print("원하는 기능의 번호를 입력해주세요 : ");
		int x = sc.nextInt();
		switch(x) {
			case 1 ->{
				if (memberService.login(username, password)) {
					loginId = memberService.getId(username);
					System.out.println("로그인 성공");
				}else {
					System.out.println("로그인 실패");
				}
			}
			case 2 -> login();
			case 3 -> {}
		}
		
		
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
		System.out.println("1. 내 정보 확인");
		System.out.println("2. 게시물 목록");
		System.out.println("3. 로그아웃");
		System.out.print("원하는 기능의 번호를 입력해주세요. : ");
		sc = new Scanner(System.in);
		int x = sc.nextInt();
		switch(x) {
		// 내정보 확인
		case 1 -> getMemberInfo();
		// 게시물 목록
		case 2 -> getBoardList();
		// 로그아웃
		case 3 -> logout();
	}
	}

	private static void logout() {
		try {
			memberService.logout(loginId);
			loginId = -1;
			System.out.println("로그아웃 성공");
		}catch (SQLException | ClassNotFoundException  e) {
			System.out.println("로그아웃 실패");
			e.printStackTrace();
		} 
	}

	private static void getBoardList() {

		
	}

	private static void getMemberInfo() {
		memberService.getmemberInfo(loginId);
	}
	
	


}
