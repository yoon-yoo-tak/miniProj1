package controller;

import java.util.List;

import DTO.MemberDto;
import service.MemberService;
import view.Console;

public class MemberController {
	private final MemberService memberService = new MemberService();
	
	public boolean register(MemberDto member) {
		return memberService.register(member);
	}
	
	public int login(String username, String password) {
		return memberService.login(username, password);
	}

	public String findId(String name, String phone) {
		return memberService.findId(name, phone);
	}

	public String findPassword(String username) {
		return memberService.findPassword(username);
	}

	public MemberDto getMemberInfo(int id) {
		return memberService.findMemberById(id);
	}
	
	public String findPasswordByKey(int id) {
		return memberService.findPasswordByKey(id);
	}

	public void updateMember(int loginId, MemberDto memberDto) {
		memberService.updateMember(loginId, memberDto);
	}

	public void deleteMember(String loginId) {
		memberService.deleteMember(loginId);
	}

	public void logout(int loginId) {
		memberService.logout(loginId);
	}
	
	public List<MemberDto> findAll(){
		return memberService.findAll();
	}
}
