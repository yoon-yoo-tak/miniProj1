package view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import DTO.BoardDto;
import DTO.MemberDto;
import controller.BoardController;
import controller.MemberController;

public class Console {
	private final MemberController memberController = new MemberController(this);
	private final BoardController boardController = new BoardController(this);
	private final Scanner sc;
	private int loginId = -1, page = 1;
	private MemberDto loginMember;
	
	public Console(Scanner sc) {
		this.sc = sc;
	}
	
	public void run() {
		while(true) {
			Runnable menu = loginId == -1 ? this::mainMenu:
							loginId == 1 ? this::adminMenu:
							this::userMenu;
			menu.run();
		}
	}
	
	private void mainMenu() {
		printOptions(new String[] {"회원가입", "로그인", "아이디 찾기", "비밀번호 찾기", "종료"});
		int oper = Integer.parseInt(getUserInput("원하는 기능의 번호를 입력해 주세요 : "));
		switch(oper) {
			case 1 -> memberController.register();
			case 2 -> {
				loginId = memberController.login();
				if (loginId != -1)
					loginMember = memberController.getMemberInfo(loginId);
				else {
					print("입력하신 정보가 존재하지 않습니다.");
				}
			}
			case 3 -> {
				String id = memberController.findId();
				if (id == null) {
					print("가입하지 않았거나 입력한 정보가 다릅니다.");
				}else {
					print("아이디는 : \"" + id + "\" 입니다.");
				}
			}
			case 4 -> {
				String password = memberController.findPassword();
				if (password == null) {
					print("가입하지 않았거나 입력한 정보가 다릅니다.");
				}else {
					print("비밀번호는 : \"" + password + "\" 입니다.");
				}
			}
			case 5 -> exit();
		}
	}
	
	private void adminMenu() {
		printOptions(new String[] {"유저 목록", "내 정보 확인", "게시물 목록", "로그아웃"});
		int oper = Integer.parseInt(getUserInput("원하는 기능의 번호를 입력해 주세요 : "));
		switch(oper) {
			case 1 -> {
				List<MemberDto> list = memberController.findAll();
				print("######## 유저 목록 ########");
				for (MemberDto m : list) {
					print("아이디 : " + m.username());
					print("이름 : " + m.name());
					print("전화번호 : " + m.phone());
					print("주소 : " + m.address());
					print("성별 : " + m.gender());
					print("");
					print("###################");
				}
				
				printOptions(new String[] {"유저 삭제", "이전 화면으로"});
				int num = Integer.parseInt(getUserInput("원하는 기능을 선택해 주세요 : "));
				if (num == 1) {
					String id = getUserInput("삭제하고자 하는 유저의 아이디를 입력해 주세요 : ");
					memberController.deleteMember(id);
				}
			}
			case 2 -> {
				MemberDto member = memberController.getMemberInfo(loginId);
				System.out.println("아이디 : " + member.username());
				System.out.println("이름 : " + member.name());
				System.out.println("전화번호 : " + member.phone());
				System.out.println("주소 : " + member.address());
				System.out.println("성별 : " + member.gender());
				memberDetailView();
			}
			case 3 -> {
				page = 1;
				listBoard();
			}
			case 4 -> {
				memberController.logout(loginId);
				loginId = -1;
				print("로그아웃 성공");
			}
			default -> print("잘못된 입력입니다. 다시 시도해주세요");
		}
	}
	
	private void userMenu() {
		printOptions(new String[] {"내 정보 확인", "게시물 목록", "로그아웃"});
		int oper = Integer.parseInt(getUserInput("원하는 기능의 번호를 입력해 주세요 : "));
		switch(oper) {
			case 1 -> {
				MemberDto member = memberController.getMemberInfo(loginId);
				System.out.println("아이디 : " + member.username());
				System.out.println("이름 : " + member.name());
				System.out.println("전화번호 : " + member.phone());
				System.out.println("주소 : " + member.address());
				System.out.println("성별 : " + member.gender());
				memberDetailView();
			}
			case 2 -> {
				page = 1;
				listBoard();
			}
			case 3 -> {
				memberController.logout(loginId);
				loginId = -1;
				print("로그아웃 성공");
			}
		}
	}
	
	public void listBoard() {
		while(true) {
			List<BoardDto> list = boardController.findAll(page);
			if (list.isEmpty()) {
				print(page + "페이지에 게시물이 존재하지 않습니다. 초기화면으로 돌아갑니다.");
				return;
			}
			printBoard(list);
			if(!handleBoardOption(list)) {
				return;
			}
		}
	}
	
	private boolean handleBoardOption(List<BoardDto> list) {
		if (loginId == 1) {
			printOptions(new String[] {"페이지 이동", "이전 페이지", "다음 페이지", "상세보기", "글쓰기", "초기화면", "게시글 삭제"});
		}else {
			printOptions(new String[] {"페이지 이동", "이전 페이지", "다음 페이지", "상세보기", "글쓰기", "초기화면"});
		}
		int oper = Integer.parseInt(getUserInput("원하는 기능의 번호를 입력해 주세요 : "));
		switch (oper) {
	        case 7 -> {
	            if (loginId == 1) {
	                deleteSelectedArticle(list);
	            }else {
	            	System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
	            }
	        }
	        case 1 -> page = Integer.parseInt(getUserInput("이동할 페이지 번호를 입력해주세요 : "));
	        case 2 -> {
	        		if (page > 1) page--;
	        		else System.out.println("이전 페이지가 존재하지 않습니다.");
	        	}
	        case 3 -> page++;
	        case 4 -> viewDetail(list);
	        case 5 -> writeArticle(loginId);
	        case 6 -> {return false;}
	        default -> System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
		}
		return true;
	}


	private void writeArticle(int loginId2) {
		System.out.println("######### 글쓰기 #########");
		String title = getUserInput("제목을 입력해 주세요 : ");
		String content = getUserInput("내용을 입력해 주세요 : ");
		String password = getUserInput("게시물 비밀번호를 입력해 주세요 : ");
		boardController.writeArticle(loginId,title, content, password);
	}

	private void viewDetail(List<BoardDto> list) {
		int num = Integer.parseInt(getUserInput("게시글 번호를 입력해 주세요 : "));
		int start = list.get(0).id();
		int end = list.getLast().id();
		if (num < start || end < num) {
			print("잘못된 번호를 입력했습니다. 이전 화면으로 돌아갑니다.");
			return;
		}
		int idx = (num - 1) % 10;
		BoardDto b = list.get(idx);
		boardController.viewArticle(b.key());
		print("#################### "+ num + "번 게시글 상세보기 ####################");
		print("번호 : " + b.id());
		print("작성자 : " + b.writer());
		print("조회수 : " + b.viewCnt());
		print("제목 : " + b.title());
		print("내용 : " + b.content());
		print("작성일 : " + formatDateTime(b.createdAt().toLocalDateTime()));
		boolean isWriter = loginId == b.memberId();
		print("1. 이전 페이지로 돌아가기");
		if (isWriter) { // 작성자
			print("2. 수정하기");
			print("3. 삭제하기");
		}
		int oper = Integer.parseInt(getUserInput("원하는 기능의 번호를 입력해 주세요 : "));
		if (oper == 1) {
			return;
		}else if (oper == 2 && isWriter) {
			updateArticle(b.key(), b.password());
		}else if (oper == 3 && isWriter) {
			deleteArticle(b.key(), b.password());
		}
	}
	
	private void deleteArticle(int key, String password) {
		String pass = getUserInput("비밀번호를 한 번 더 입력해 주세요. : ");
		if (pass.equals(password)) 
			boardController.deleteArticle(key);
		else {
			System.out.println("비밀번호가 일치하지 않습니다. 초기화면으로 돌아갑니다.");
		}
	}

	private void updateArticle(int key, String password) {
		String pass = getUserInput("비밀번호를 한 번 더 입력해 주세요. : ");
		if (pass.equals(password)) {
			String title = getUserInput("변경할 제목을 입력해주세요. : ");
			String content = getUserInput("변경할 내용을 입력해주세요. : ");
			boardController.updateArticle(key, title, content);
		}
		else {
			System.out.println("비밀번호가 일치하지 않습니다. 초기화면으로 돌아갑니다.");
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

	private void deleteSelectedArticle(List<BoardDto> list) {
		int num = Integer.parseInt(getUserInput("삭제할 게시글의 번호를 입력해 주세요 : "));
		num = (num - 1) % 10;
		if (0 <= num && num < list.size()) {
			boardController.deleteArticle(list.get(num).key());
		}else print("잘못된 번호입니다. 초기 화면으로 돌아갑니다.");
	}

	private void printBoard(List<BoardDto> list) {
        System.out.println("############ " + page + "페이지 게시물 목록 ############");
        for (BoardDto b : list) {
     	   System.out.println(b.id() + " || " + b.writer() + " || " + b.title() + " || " + b.content() + " || " + b.viewCnt() + " || " + formatDateTime(b.createdAt().toLocalDateTime()));
        }
    }
	
	public void memberDetailView() {
		printOptions(new String[] {"내 정보 수정", "회원 탈퇴", "이전 화면으로"});
		int oper = Integer.parseInt(getUserInput("원하는 기능의 번호를 입력해 주세요 : "));
		switch(oper) {
			case 1 -> {
				String password = getUserInput("비밀번호를 한 번 더 입력해 주세요 : ");
				if (memberController.findPasswordByKey(loginId).equals(password)) {
					updateMember();
				}else {
					System.out.println("비밀번호가 일치하지 않습니다.");
				}
			}
			case 2 -> {
				memberController.deleteMember(loginMember.username());
				loginId = -1;
			} 
			case 3 -> {}
		}
	}
	
	public void updateMember() {
		print("정보를 수정합니다.");
		String username = getUserInput("변경할 아이디 : ");
		String password = getUserInput("변경할 비밀번호 : ");
		String name = getUserInput("변경할 이름 : ");
		String phone = getUserInput("변경할 전화번호 : ");
		String address = getUserInput("변경할 주소 : ");
		printOptions(new String[] {"변경하기", "다시 입력", "이전 화면으로"});
		int oper = Integer.parseInt(getUserInput("원하는 기능의 번호를 입력해 주세요 : "));
		
		switch(oper) {
			case 1 -> {
				memberController.updateMember(loginId, new MemberDto(username, loginMember.password(), name, phone, address, loginMember.gender()));
			}
			case 2 -> updateMember();
			case 3 -> {}
		}
	}
	
	public String getUserInput(String message) {
		System.out.println(message);
		String s = sc.next();
		sc.nextLine();
		return s;
	}
	
	private void printOptions(String[] options) {
		for (int i = 0; i < options.length; i++) {
			System.out.println(i + 1 + ". " + options[i]);
		}
	}
	
	private void exit() {
        System.out.println("프로그램을 종료합니다.");
        sc.close();
        System.exit(0);
    }
	
	public void print(String message) {
		System.out.println(message);
	}
}
