package DTO;

import java.sql.Timestamp;

public record BoardDto(
		int id,
		int key,
		int memberId,
		String title,
		String content,
		String password,
		int viewCnt,
		Timestamp createdAt,
		String writer) {}
