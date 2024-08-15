package miniProj1;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import DTO.BoardDto;
import Service.BoardService;
import Service.MemberService;

public class Main {
	
	private static Scanner sc;
	private static MemberService memberService = new MemberService();
	private static BoardService boardService = new BoardService();
	private static int loginId = -1;
	
	public static void main(String[] args) {
		while(true) {
			if (loginId == -1) {
				mainMenu();
			}else if (loginId == 1){ // 관리자 메뉴
				
			}else {
				userMenu();
			}
		}
		
	}
	
	private static void mainMenu() {
		System.out.println("======================");
		System.out.println("1. 회원가입");
		System.out.println("2. 로그인");
		System.out.println("3. 아이디 찾기");
		System.out.println("4. 비밀번호 찾기");
		System.out.println("5. 종료");
		System.out.print("원하는 기능의 번호를 입력해주세요. : ");
		sc = new Scanner(System.in);
		int x = sc.nextInt();
		switch(x) {
			// 회원가입
			case 1 -> register();
			// 로그인
			case 2 -> login();
			// 아이디 찾기
			case 3 -> findId();
			// 비밀번호 찾기
			case 4 -> findPassword();
			case 5 -> {
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
		case 2 -> listBoardAndHandling();
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

	// 처음에는 그냥 1페이지 보여주기
	private static void listBoardAndHandling() {
		int curPage = 1;
		boolean isContinue = true;
		while (isContinue) {
			List<BoardDto> list = boardService.getBoardList(curPage);
			if (list.size() == 0) {
				System.out.println(curPage + "페이지에 게시물이 존재하지 않습니다. 초기 화면으로 돌아갑니다.");
				return;
			}
			printBoard(list, curPage);
			System.out.println("""
					1. 페이지 이동
					2. 이전 페이지로 이동
					3. 다음 페이지로 이동
					4. 게시글 상세보기
					5. 글 쓰기
					6. 이전 화면으로
					원하는 기능을 선택하세요 : """);
			sc = new Scanner(System.in);
			int oper = sc.nextInt();
			switch(oper) {
				// 특정 페이지로 이동
				case 1 -> {
					System.out.print("이동할 페이지 번호를 입력하세요 : ");
					int pageNum = sc.nextInt();
					if (pageNum > 0) {
						curPage = pageNum;
					}else {
						System.out.println("잘못된 페이지 번호입니다.");
					}
				}
				
				// 이전 페이지로
				case 2 -> {
					if (curPage > 1) curPage--;
					else System.out.println("이전 페이지가 존재하지 않습니다.");
				}
				
				// 다음 페이지로
				case 3 -> curPage++;
				
				// 상세보기
				case 4 -> {
					viewDetail(list);
				}
				// 글쓰기
				case 5 -> {}
				// 끝내기
				case 6 -> {
					isContinue = false;
				}
			}
		}
	}


	private static void getMemberInfo() {
		memberService.getmemberInfo(loginId);
		sc = new Scanner(System.in);
		System.out.println("원하는 기능의 번호를 입력해주세요.");
		System.out.println("1. 내 정보 수정");
		System.out.println("2. 회원탈퇴");
		System.out.println("3. 이전화면으로");
		int x = sc.nextInt();
		
		switch(x) {
			// 내정보 수정
			case 1 -> {
				System.out.print("비밀번호를 한 번 더 입력해 주세요. : ");
				sc = new Scanner(System.in);
				String password = sc.nextLine();
				if (checkPassword(loginId, password))
					updateMember();
				else {
					System.out.println("비밀번호가 일치하지 않습니다. 초기화면으로 돌아갑니다.");
				}
				}
			// 회원탈퇴
			case 2 -> deleteMember();
			// 이전화면으로
			case 3 -> {}
		}
	}

	private static boolean checkPassword(int loginId, String password) {
		return memberService.checkPassword(loginId, password);
	}

	private static void deleteMember() {
		 memberService.deleteMember(loginId);
		 loginId = -1;
	}

	private static void updateMember() {
		sc = new Scanner(System.in);
		System.out.println("정보를 수정합니다.");
		System.out.println("변경할 아이디 : ");
		String username = sc.nextLine();
		System.out.println("변경할 비밀번호 : ");
		String password = sc.nextLine();
		System.out.println("변경할 이름 : ");
		String name = sc.nextLine();
		System.out.println("변경할 전화번호 : ");
		String phone = sc.nextLine();
		System.out.println("변경할 주소 : ");
		String address = sc.nextLine();
		
		System.out.println("원하는 기능의 번호를 입력해주세요 : ");
		System.out.println("1. 변경하기");
		System.out.println("2. 다시 입력");
		System.out.println("3. 이전 화면으로");
		int x = sc.nextInt();
		switch(x) {
			case 1 -> {
				memberService.updateMember(loginId, username, password, name, phone, address);
				}
			case 2 -> updateMember();
			case 3 -> {}
		}
	}
	
	
	public static void findId() {
		sc = new Scanner(System.in);
		System.out.println("아이디 찾기");
		System.out.print("이름을 입력해 주세요 : ");
		String name = sc.nextLine();
		System.out.print("전화번호를 입력해 주세요 : ");
		String phone = sc.nextLine();
		String id = memberService.findId(name, phone);
		if (id == null) {
			System.out.println("가입하지 않았거나 입력하신 정보가 다릅니다.");
		}else {
			System.out.println("아이디 : " + id);
		}
	}
	
	public static void findPassword() {
		sc = new Scanner(System.in);
		System.out.println("비밀번호 찾기");
		System.out.print("아이디를 입력해 주세요 : ");
		String userId = sc.nextLine();
		String password = memberService.findPassword(userId);
		if (password == null) {
			System.out.println("가입하지 않은 아이디거나 입력하신 정보가 다릅니다.");
		}else {
			System.out.println("비밀번호 : " + password);
		}
	}
	
	private static String formatDateTime(LocalDateTime dateTime) {
		LocalDateTime now = LocalDateTime.now();
		long diff = java.time.Duration.between(dateTime, now).toHours();
		if (diff < 24) {
			return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
		}
		return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	private static void printBoard(List<BoardDto> list, int page) {
		System.out.println("#################### "+page + "페이지 ####################");
		for (BoardDto b: list) {
			System.out.println(b.id() + " || " + b.writer() + " || " + b.title() + " || " + b.viewCnt() + " || " + formatDateTime(b.createdAt().toLocalDateTime()));
		}
	}
	
	public static void viewDetail(List<BoardDto> list) {
		System.out.println("게시글 번호를 입력해 주세요. : ");
		sc = new Scanner(System.in);
		int id = sc.nextInt();
		int start = list.get(0).id();
		int end = list.getLast().id();
		if (id < start || end < id) {
			System.out.println("잘못된 번호를 입력했습니다. 이전 화면으로 돌아갑니다.");
			return;
		}
		int idx = (id - 1) % 10;
		BoardDto b = list.get(idx);
		boardService.increaseViewCnt(b.key());
		System.out.println("#################### "+id + "번 게시글 상세보기 ####################");
		System.out.println(b.id() + " || " + b.writer() + " || " + b.title() + " || " + b.viewCnt() + " || " + formatDateTime(b.createdAt().toLocalDateTime()));
		boolean isWriter = loginId == b.memberId();
		System.out.println("1. 이전 페이지로 돌아가기");
		if (isWriter) { // 작성자
			System.out.println("2. 수정하기");
			System.out.println("3. 삭제하기");
		}
		int oper = sc.nextInt();
		
		if (oper == 1) {
			return;
		}else if (oper == 2 && isWriter) {
			updateArticle(b.id());
		}else if (oper == 3 && isWriter) {
			deleteArticle(b.id());
		}
	}

	private static void deleteArticle(int id) {
		System.out.print("비밀번호를 한 번 더 입력해 주세요. : ");
		sc = new Scanner(System.in);
		String password = sc.nextLine();
		if (checkPassword(loginId, password))
			boardService.deleteArticle(id);
		else {
			System.out.println("비밀번호가 일치하지 않습니다. 초기화면으로 돌아갑니다.");
		}
	}

	private static void updateArticle(int id) {
		System.out.print("비밀번호를 한 번 더 입력해 주세요. : ");
		sc = new Scanner(System.in);
		String password = sc.nextLine();
		if (checkPassword(loginId, password)) {
			System.out.println("변경할 제목을 입력해주세요. : ");
			String title = sc.nextLine();
			System.out.println("변경할 내용을 입력해주세요. : ");
			String content = sc.nextLine();
			boardService.updateArticle(id, title, content);
		}
		else {
			System.out.println("비밀번호가 일치하지 않습니다. 초기화면으로 돌아갑니다.");
		}
	}
}
