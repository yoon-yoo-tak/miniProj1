package service;

import java.util.List;

import DTO.MemberDto;
import repository.MemberRepository;
import repository.MemberRepositoryImpl;

public class MemberService {
	private final MemberRepository memberRepository = new MemberRepositoryImpl();
	
	public boolean register(MemberDto member) {
		return memberRepository.registerMember(member);
	}
	
	public int login(String username, String password) {
		boolean ret = memberRepository.login(username, password);
		int id = -1;
		if (ret) {
			id = memberRepository.findIdByUsername(username);
			memberRepository.recordLogin(id);
		}
		return id;
	}
	
	public int getId(String username) {
		return memberRepository.findIdByUsername(username);
	}
	
	public void logout(int id) {
		memberRepository.recordLogout(id);
	}
	
	public MemberDto findMemberById(int id) {
		return memberRepository.findMemberById(id);
	}
	
	public String findId(String name, String phone) {
		return memberRepository.findIdByNameAndPhone(name, phone);
	}
	
	public boolean updateMember(int loginId, MemberDto member) {
		return memberRepository.updateMember(loginId, member) == 1 ? true : false;
	}
	
	public String findPassword(String userId) {
		return memberRepository.findPasswordByUserId(userId);
	}
	
	public String findPasswordByKey(int id) {
		return memberRepository.findPassword(id);
	}
	
	public void deleteMember(String id) {
		memberRepository.deleteMemberById(id);
	}
	
	public String findName(int id) {
		return memberRepository.findNameById(id);
	}
	
	public List<MemberDto> findAll(){
		return memberRepository.findAll();
	}
	
}
