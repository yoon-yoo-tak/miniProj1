package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DTO.MemberDto;
import util.DBUtil;

public class MemberRepositoryImpl implements MemberRepository{
	
	private DBUtil db = new DBUtil();

	@Override
	public boolean registerMember(MemberDto member) {
		String query = """
				INSERT INTO MEMBER (ID, USERID, PASSWORD, NAME, PHONE, ADDRESS, GENDER)
				VALUES (member_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)
				""";
		try(Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query)){
			
			pstmt.setString(1, member.username());
			pstmt.setString(2, member.password());
			pstmt.setString(3, member.name());
			pstmt.setString(4, member.phone());
			pstmt.setString(5, member.address());
			pstmt.setString(6, member.gender());
			pstmt.executeQuery();
			return true;
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
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
				recordLogin(id);
				return true;
			}
			return false;
			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void recordLogin(int id) {
		String procedure = "{call login_record(?)}";
		try (Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(procedure);){
			pstmt.setInt(1, id);
			pstmt.execute();
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void recordLogout(int id) {
		String logout_record = "{call logout_record(?)}";
		try(Connection conn = db.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(logout_record);){
			pstmt.setInt(1, id);
			pstmt.execute();			
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int findIdByUsername(String username) {
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

	@Override
	public MemberDto findMemberById(int id) {
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
				return new MemberDto(rs.getString("userid"),
						rs.getString("password"),
						rs.getString("name"),
						rs.getString("phone"),
						rs.getString("address"),
						rs.getString("gender"));
			}
				
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateMember(int loginId, MemberDto member) {
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
				pstmt.setString(1, member.username());
				pstmt.setString(2, member.password());
				pstmt.setString(3, member.name());
				pstmt.setString(4, member.phone());
				pstmt.setString(5, member.address());
				pstmt.setInt(6, loginId);
				int ret = pstmt.executeUpdate();
				return ret;
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public String findPassword(int loginId) {
		String query = """
				select *
				from member
				where id = ?
				""";
		try(Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);)
		{
			pstmt.setInt(1, loginId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString("password");
			}
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			
		}
		return null;
	}

	@Override
	public String findIdByNameAndPhone(String name, String phone) {
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

	@Override
	public String findPasswordByUserId(String userId) {
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

	@Override
	public void deleteMemberById(String id) {
		String procedure = "{call delete_member(?)}";
		try(Connection conn = db.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(procedure);){
				pstmt.setString(1, id);
				pstmt.execute();
				System.out.println("회원 탈퇴를 완료했습니다.");
		}catch(SQLException | ClassNotFoundException e) {
			System.out.println("회원 탈퇴 실패");
			e.printStackTrace();
		}
	}

	@Override
	public String findNameById(int id) {
		String query = """
				SELECT NAME
				FROM MEMBER
				WHERE ID = ?
				""";
		try(Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);){
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) return rs.getString("name");
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<MemberDto> findAll() {
		String query = """
				SELECT *
				FROM MEMBER
				WHERE ISDEL = '0'
				AND ID != 1
				""";
		List<MemberDto> list = null;
		try(Connection conn = db.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(query);){
			ResultSet rs = pstmt.executeQuery();
			list = new ArrayList<>();
			System.out.println(list.size());
			while (rs.next()) {
				list.add(new MemberDto(rs.getString("userid"),
						rs.getString("password"),
						rs.getString("name"),
						rs.getString("phone"),
						rs.getString("address"),
						rs.getString("gender"))
						);
			}
		}catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

}
