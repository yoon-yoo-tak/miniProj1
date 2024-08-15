package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import DTO.BoardDto;
import util.DBUtil;

public class BoardService {
	private DBUtil db = new DBUtil();
	
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
}
