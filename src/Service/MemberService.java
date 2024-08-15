package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.DBUtil;

public class MemberService {
	private DBUtil db = new DBUtil();
	
	public boolean registerMember(String username, String password, String name, String phone, String address, String gender) {
		String query = """
				INSERT INTO MEMBER (ID, USERID, PASSWORD, NAME, PHONE, ADDRESS, GENDER)
				VALUES (member_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)
				""";
		try(Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)){
			
			if (gender.length() != 1) return false;
			if (gender.equals("남")) gender = "0";
			else if (gender.equals("여")) gender = "0";
			else return false;
			
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, name);
			pstmt.setString(4, phone);
			pstmt.setString(5, address);
			pstmt.setString(6, gender);
			pstmt.executeQuery();
			return true;
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
}
