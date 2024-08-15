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
				else return -1;
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

	public void getmemberInfo(int id) {
		String query = """
				SELECT *
				FROM MEMBER
				WHERE ID = ?
				""";
		try(Connection conn = db.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query);){
				pstmt.setInt(1, id);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					System.out.println("아이디 : " + rs.getString("userid"));
					System.out.println("이름 : " + rs.getString("name"));
					System.out.println("전화번호 : " + rs.getString("phone"));
					System.out.println("주소 : " + rs.getString("address"));
					String gender = rs.getString("gender");
					System.out.println("성별 : " + (gender.equals("0") ? "남자" : "여자"));
				}
				
			}catch(SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				
			}
	}

	public void updateMember(int loginId, String username, String password, String name, String phone,
			String address) {
		String query = """
				UPDATE MEMBER
				SET
				USERID = ?,
				PASSWORD = ?,
				NAME = ?,
				PHONE = ?,
				ADDRESS = ?
				WHERE ID = ?
				""";
		try(Connection conn = db.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query);){
				pstmt.setString(1, username);
				pstmt.setString(2, password);
				pstmt.setString(3, name);
				pstmt.setString(4, phone);
				pstmt.setString(5, address);
				pstmt.setInt(6, loginId);
				int ret = pstmt.executeUpdate();
				if (ret == 1) {
					System.out.println("회원 정보 수정 성공.");
				}else {
					System.out.println("회원 정보 수정 실패");
				}
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			
		}
	}

	public boolean checkPassword(int loginId, String password) {
		String query = """
				select *
				from member
				where id = ?
				""";
		try(Connection conn = db.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query);){
				pstmt.setInt(1, loginId);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getString("password").equals(password);
				}
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			
		}
		return false;
	}
	
	public String findId(String name, String phone) {
		String query = """
				SELECT *
				FROM MEMBER
				WHERE NAME = ?
				AND PHONE = ?
				""";
		try(Connection conn = db.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query);){
				pstmt.setString(1, name);
				pstmt.setString(2, phone);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getString("userid");
				}
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	public String findPassword(String userId) {
		String query = """
				SELECT *
				FROM MEMBER
				WHERE USERID = ?
				""";
		try(Connection conn = db.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query);){
				pstmt.setString(1, userId);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					return rs.getString("password");
				}
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			
		}
		return null;
	}

}
