package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	
	public boolean login(String username, String password) {
		String query = """
				SELECT ID
				FROM MEMBER
				WHERE USERID = ?
				AND PASSWORD = ?
				AND ISDEL = '0'
				""";
		try(Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);){
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt("id");
				loginRecord(id);
				return true;
			}
			return false;
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void loginRecord(int id) {
		String procedure = "{call login_record(?)}";
		try (Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(procedure);){
			pstmt.setInt(1, id);
			pstmt.execute();
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public int getId(String username) {
		String query = """
				SELECT ID
				FROM MEMBER
				WHERE USERID = ?
				""";
		try (Connection conn = db.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query);){
				pstmt.setString(1, username);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) return rs.getInt("id");
			}catch(SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		return -1;
	}
	
	public void logout(int id) throws SQLException, ClassNotFoundException{
		String logout_record = "{call logout_record(?)}";
		Connection conn = db.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(logout_record);
		pstmt.setInt(1, id);
		pstmt.execute();
	}
}
