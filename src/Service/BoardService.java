package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.BoardDto;
import util.DBUtil;

public class BoardService {
	private DBUtil db = new DBUtil();
	private MemberService memberService = new MemberService();
	
	public List<BoardDto> getBoardList(int page) {
		int pageSize = 10;
		int start = (page - 1) * pageSize + 1;
		int end = start + 9;
		List<BoardDto> list = new ArrayList<>();
		String query = """
				select a.* 
				from (
				    select rownum as rnum, b.*
				    from (
				        select *
				        from board
				        where isdel = '0'
				        order by createdAt desc
				    )b
				    where rownum <= ?
				)a
				where rnum >= ?
				""";
		try(Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)){
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new BoardDto(
						rs.getInt("rnum"),
						rs.getInt("id"),
						rs.getInt("memberId"),
						rs.getString("title"),
						rs.getString("content"),
						rs.getString("password"),
						rs.getInt("viewCnt"),
						rs.getTimestamp("createdAt"),
						rs.getString("writer")
						));
			}
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void increaseViewCnt(int boardId) {
		String query = """
				UPDATE BOARD
				SET VIEWCNT = VIEWCNT + 1
				WHERE ID = ?
				""";
		try(Connection conn = db.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query)){
				pstmt.setInt(1, boardId);
				pstmt.executeUpdate();				
			}catch(SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
	}

	public void deleteArticle(int id) {
		String query = """
				UPDATE BOARD
				SET ISDEL = '1'
				WHERE ID = ?
				""";
		System.out.println(id);
		try(Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)){
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			System.out.println("삭제 완료");
		}catch(SQLException | ClassNotFoundException e) {
			System.out.println("삭제 실패");
			e.printStackTrace();
		}
	}

	public void updateArticle(int id, String title, String content) {
		String query = """
				UPDATE BOARD
				SET TITLE = ?,
				CONTENT = ?
				WHERE ID = ?
				""";
		try(Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)){
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, id);
			int x = pstmt.executeUpdate();
			System.out.println("수정 완료 " + x);
		}catch(SQLException | ClassNotFoundException e) {
			System.out.println("수정 실패");
			e.printStackTrace();
		}
	}

	public void writeArticle(int loginId, String title, String content, String password) {
		String name = memberService.getName(loginId);
		String query = """
				INSERT INTO BOARD (ID, MEMBERID, TITLE, CONTENT, PASSWORD, CREATEDAT, WRITER)
				VALUES (BOARD_SEQ.NEXTVAL, ?, ?, ?, ?, SYSTIMESTAMP, ?) 
				""";
		try(Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)){
			pstmt.setInt(1, loginId);
			pstmt.setString(2, title);
			pstmt.setString(3, content);
			pstmt.setString(4, password);
			pstmt.setString(5, name);
			pstmt.executeQuery();		
			System.out.println("등록 완료");
		}catch(SQLException | ClassNotFoundException e) {
			System.out.println("등록 실패");
			e.printStackTrace();
		}
	}
}
