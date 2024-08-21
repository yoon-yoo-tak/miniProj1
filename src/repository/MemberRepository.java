package repository;

import java.util.List;

import DTO.MemberDto;

public interface MemberRepository {
	boolean registerMember(MemberDto member);
	boolean login(String username, String password);
	void recordLogin(int id);
	void recordLogout(int id);
	int findIdByUsername(String username);
	MemberDto findMemberById(int id);
	int updateMember(int loginId, MemberDto member);
	String findPassword(int loginId);
	String findIdByNameAndPhone(String name, String phone);
	String findPasswordByUserId(String userId);
	void deleteMemberById(String id);
	String findNameById(int id);
	List<MemberDto> findAll();
}
