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
	
	public boolean login(String username, String password) {
		boolean ret = memberRepository.login(username, password);
		if (ret) {
			int id = memberRepository.findIdByUsername(username);
			memberRepository.recordLogin(id);
		}
		return ret;
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
	
	public boolean updateMember(int loginId, MemberDto member) {
		return memberRepository.updateMember(loginId, member) == 1 ? true : false;
	}
	
	public String findPassword(String userId) {
		return memberRepository.findPasswordByUserId(userId);
	}
	
	public void deleteMember(int id) {
		memberRepository.deleteMemberById(id);
	}
	
	public String findName(int id) {
		return memberRepository.findNameById(id);
	}
	
	public List<MemberDto> findAll(){
		return memberRepository.findAll();
	}
	
}
