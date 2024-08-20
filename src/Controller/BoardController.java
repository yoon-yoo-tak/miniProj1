package Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import DTO.BoardDto;
import Service.BoardService;
import util.DBUtil;
import util.IOUtil;

public class BoardController {
	
	private final BoardService boardService = new BoardService();
	private final Scanner sc;
	private static int curPage = 1;
	public BoardController(Scanner sc) {
		this.sc = sc;
	}

	public void listBoardAndHandling(int loginId) {
		curPage = 1;
        while (true) {
            List<BoardDto> list = boardService.getBoardList(curPage);
            if (list.isEmpty()) {
                System.out.println(curPage + "페이지에 게시물이 존재하지 않습니다. 초기 화면으로 돌아갑니다.");
                return;
            }
            printBoard(list, curPage);
            if (!handleBoardOptions(list, loginId)) {
                return;
            }
        }
	}


    private void printBoard(List<BoardDto> list, int curPage) {
        System.out.println("############ " + curPage + "페이지 게시물 목록 ############");
        for (BoardDto b : list) {
     	   System.out.println(b.id() + " || " + b.writer() + " || " + b.title() + " || " + b.content() + " || " + b.viewCnt() + " || " + formatDateTime(b.createdAt().toLocalDateTime()));
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

	private boolean handleBoardOptions(List<BoardDto> list, int loginId) {
		if (loginId == 1) {
            System.out.println("0. 게시글 삭제");
        }
        printBoardMenuOptions();
        int choice = IOUtil.getInput(sc);

        switch (choice) {
            case 0 -> {
                if (loginId == 1) {
                    deleteSelectedArticle(list);
                }
            }
            case 1 -> moveToPage();
            case 2 -> {
            		if (curPage > 1) curPage--;
            		else System.out.println("이전 페이지가 존재하지 않습니다.");
            	}
            case 3 -> curPage++;
            case 4 -> viewDetail(list, loginId);
            case 5 -> writeArticle(loginId);
            case 6 -> {return false;}
            default -> System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
        }
        return true;
    }

	private void printBoardMenuOptions() {
        System.out.println("1. 페이지 이동");
        System.out.println("2. 이전 페이지");
        System.out.println("3. 다음 페이지");
        System.out.println("4. 상세보기");
        System.out.println("5. 글쓰기");
        System.out.println("6. 초기화면");
    }
	
    private void deleteSelectedArticle(List<BoardDto> list) {
        System.out.print("삭제할 게시물의 번호를 입력해주세요: ");
        int articleNum = IOUtil.getInput(sc);
        articleNum = (articleNum - 1) % 10;
        if (articleNum >= 0 && articleNum < list.size()) {
            boardService.deleteArticle(list.get(articleNum).key());
        } else {
            System.out.println("잘못된 번호입니다. 초기 화면으로 돌아갑니다.");
        }
	}
    
    private void moveToPage() {
    	System.out.print("이동할 페이지를 입력해 주세요 : ");
        curPage = IOUtil.getInput(sc);
	}
    
	private void writeArticle(int loginId) {
		System.out.println("######### 글쓰기 #########");
		System.out.print("제목을 입력해 주세요 : ");
		String title = IOUtil.getString(sc);
		System.out.print("내용을 입력해 주세요 : ");
		String content = IOUtil.getString(sc);
		System.out.print("게시물 비밀번호를 입력해 주세요 : ");
		String password = IOUtil.getString(sc);
		boardService.writeArticle(loginId,title, content, password);
	}

	private void viewDetail(List<BoardDto> list, int loginId) {
		System.out.println("게시글 번호를 입력해 주세요. : ");
		int id = IOUtil.getInput(sc);
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
		System.out.println(b.id() + " || " + b.writer() + " || " + b.title() + " || " + b.content() + " || " + b.viewCnt() + " || " + formatDateTime(b.createdAt().toLocalDateTime()));
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
			updateArticle(b.key(), b.password());
		}else if (oper == 3 && isWriter) {
			deleteArticle(b.key(), b.password());
		}
	}

	private void updateArticle(int id, String articlePassword) {
		System.out.print("비밀번호를 한 번 더 입력해 주세요. : ");
		String password = IOUtil.getString(sc);
		if (password.equals(articlePassword)) {
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

	private void deleteArticle(int id, String articlePassword) {
		System.out.print("비밀번호를 한 번 더 입력해 주세요. : ");
		String password = IOUtil.getString(sc);
		if (password.equals(articlePassword)) 
			boardService.deleteArticle(id);
		else {
			System.out.println("비밀번호가 일치하지 않습니다. 초기화면으로 돌아갑니다.");
		}
	}
}
