package service;

import java.util.List;

import DTO.BoardDto;
import repository.BoardRepository;
import repository.BoardRepositoryImpl;
import repository.MemberRepository;
import repository.MemberRepositoryImpl;

public class BoardService {
	private final BoardRepository boardRepository = new BoardRepositoryImpl();
	private final MemberRepository memberRepository = new MemberRepositoryImpl();
	private static final int PAGE_SIZE = 10;
	
	public List<BoardDto> findAll(int page){
		int start = (page - 1) * PAGE_SIZE + 1;
		int end = page * PAGE_SIZE;
		return boardRepository.findAll(start, end);
	}
	
	public void viewArticle(int boardId) {
		boardRepository.updateViewCnt(boardId);
	}
	
	public void deleteArticle(int boardId) {
		boardRepository.deleteById(boardId);
	}
	
	public void updateArticle(int boardId, String title, String content) {
		boardRepository.updateArticle(boardId, title, content);
	}
	
	public void createArticle(int loginId, String title, String content, String password, int id) {
		String name = memberRepository.findNameById(id);
		boardRepository.saveArticle(loginId, title, content, password, name);
	}
}
