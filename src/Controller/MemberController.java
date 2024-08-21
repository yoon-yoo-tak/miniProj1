package controller;

import java.util.List;

import DTO.MemberDto;
import service.MemberService;
import view.Console;

public class MemberController {
	private final MemberService memberService = new MemberService();
	private final Console console;
	
	public MemberController(Console console) {
		this.console = console;
	}
	
	public void register() {
		String username = console.getUserInput("아이디를 입력해 주세요 : ");
		String password = console.getUserInput("비밀번호를 입력해 주세요 : ");
		String name = console.getUserInput("이름을 입력해 주세요 : ");
		String phone = console.getUserInput("전화번호를 입력해 주세요 : ");
		String address = console.getUserInput("주소를 입력해 주세요 : ");
		String gender = console.getUserInput("성별을 입력해 주세요(남/여) :" );
		
		boolean success = memberService.register(new MemberDto(username, password, name, phone, address, gender));
		if (success) {
			console.print("회원가입 성공");
		}else {
			console.print("회원가입 실패");
		}
	}
	
	public int login() {
		String usernmae = console.getUserInput("아이디를 입력해 주세요 : ");
		String password = console.getUserInput("비밀번호를 입력해 주세요 : ");
		return memberService.login(usernmae, password);
	}

	public String findId() {
		String name = console.getUserInput("이름을 입력해 주세요 : ");
		String phone = console.getUserInput("전화번호을 입력해 주세요 : ");
		return memberService.findId(name, phone);
	}

	public String findPassword() {
		String username = console.getUserInput("아이디를 입력해 주세요 : ");
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
