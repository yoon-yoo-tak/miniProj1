package repository;

import java.util.List;

import DTO.BoardDto;

public interface BoardRepository {
	List<BoardDto> findAll(int start, int end);
	void updateViewCnt(int boardId);
	void deleteById(int boardId);
	void updateArticle(int id, String title, String content);
	void saveArticle(int loginId, String title, String content, String password, String nmae);
}
