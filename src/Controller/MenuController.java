package Controller;

import java.util.Scanner;

import util.IOUtil;

public class MenuController {
	private final Scanner sc = new Scanner(System.in);
	private final MemberController memberController = new MemberController(sc);
    private final BoardController boardController = new BoardController(sc);
	private int loginId = -1;
	
	public void run() {
		while (true) {
			if (loginId == -1) { // 로그인 안한 상태
				mainMenu();
			}else if (loginId == 1) { // admin
				adminMenu();
			}else { // 로그인한 상태
				userMenu();
			}
		}
	}

	/**
	 * 메인메뉴 처리
	 */
	private void mainMenu() {
		printMainOption();
		int oper = IOUtil.getInput(sc);
		switch(oper) {
			case 1 -> memberController.register();
			case 2 -> loginId = memberController.login();
			case 3 -> memberController.findId();
			case 4 -> memberController.findPassword();
			case 5 -> exit();
			default -> System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
			
		}
	}
	
	private void printMainOption() {
		System.out.println("======================");
        System.out.println("1. 회원가입");
        System.out.println("2. 로그인");
        System.out.println("3. 아이디 찾기");
        System.out.println("4. 비밀번호 찾기");
        System.out.println("5. 종료");
        System.out.print("원하는 기능의 번호를 입력해주세요. : ");
	}
	

	//	TODO : 유저랑 어드민 겹침.	
	/**
	 * 로그인후 메뉴 처리
	 */
	private void printUserOptions() {
		System.out.println("1. 내 정보 확인");
        System.out.println("2. 게시물 목록");
        System.out.println("3. 로그아웃");
        System.out.print("원하는 기능의 번호를 입력해주세요. : ");
	}
	
    private void userMenu() {
        printUserOptions();
        int choice = IOUtil.getInput(sc);
        switch (choice) {
            case 1 -> memberController.getMemberInfo(loginId);
            case 2 -> boardController.listBoardAndHandling(loginId);
            case 3 -> logout();
            default -> System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
        }
    }
	
	
	/*
	 * Admin메뉴 처리
	 */
	private void adminMenu() {
		printAdminMenuOption();
		int oper = IOUtil.getInput(sc);
		switch(oper) {
			case 0 -> memberController.getUserList();
	        case 1 -> memberController.getMemberInfo(loginId);
	        case 2 -> boardController.listBoardAndHandling(loginId);
	        case 3 -> logout();
	        default -> System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
		}
	}

	private void printAdminMenuOption() {
		System.out.println("0. 유저 목록");
        System.out.println("1. 내 정보 확인");
        System.out.println("2. 게시물 목록");
        System.out.println("3. 로그아웃");
        System.out.print("원하는 기능의 번호를 입력해주세요. : ");
	}
	
	
	private void logout() {
		loginId = -1;
		System.out.println("로그아웃 성공");
	}
	
	private void exit() {
        System.out.println("프로그램을 종료합니다.");
        sc.close();
        System.exit(0);
    }
}
