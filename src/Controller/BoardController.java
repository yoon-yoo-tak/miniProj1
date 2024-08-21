package controller;

import java.util.List;

import DTO.BoardDto;
import service.BoardService;
import view.Console;

public class BoardController {
	
	private final BoardService boardService = new BoardService();
	private final Console console;
	
	public BoardController(Console console) {
		this.console = console;
	}

	public List<BoardDto> findAll(int page) {
		return boardService.findAll(page);
	}

	public void deleteArticle(int key) {
		boardService.deleteArticle(key);
	}

	public void viewArticle(int key) {
		boardService.viewArticle(key);
	}

	public void updateArticle(int key, String title, String content) {
		boardService.updateArticle(key, title, content);
	}

	public void writeArticle(int loginId, String title, String content, String password) {
		boardService.createArticle(loginId, title, content, password, loginId);
	}
}
