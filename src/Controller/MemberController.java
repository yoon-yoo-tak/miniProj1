package Controller;

import java.util.Scanner;

import Service.MemberService;
import util.IOUtil;

public class MemberController {
	
	private final MemberService memberService = new MemberService();
	private final Scanner sc;
	
	public MemberController(Scanner sc) {
		this.sc = sc;
	}

	public void register() {
		System.out.println("회원 가입 화면");
        System.out.print("아이디: ");
        String username = IOUtil.getString(sc);

        System.out.print("비밀번호: ");
        String password = IOUtil.getString(sc);

        System.out.print("이름: ");
        String name = IOUtil.getString(sc);

        System.out.print("전화번호: ");
        String phone = IOUtil.getString(sc);

        System.out.print("주소: ");
        String address = IOUtil.getString(sc);

        System.out.print("성별 (남/여): ");
        String gender = IOUtil.getString(sc);

        memberService.registerMember(username, password, name, phone, address, gender);
	}
	
	public int login() {
        System.out.print("아이디: ");
        String username = IOUtil.getString(sc);
        System.out.print("비밀번호: ");
        String password = IOUtil.getString(sc);

        if (memberService.login(username, password)) {
            System.out.println("로그인 성공");
            return memberService.getId(username);
        } else {
            System.out.println("로그인 실패");
            return -1;
        }
    }
	
	public void getUserList() {
        memberService.getUserList();
        System.out.println("1. 유저 삭제");
		System.out.println("2. 이전 화면으로");
		System.out.print("원하는 기능을 선택해 주세요 : ");
		int oper = IOUtil.getInput(sc);
		if (oper == 1) {
			System.out.print("삭제하고자 하는 유저의 번호를 입력해 주세요 : ");
			int num = IOUtil.getInput(sc);
			deleteMemberAdmin(num);
		}
    }
	

	public int getMemberInfo(int loginId) {
		int id = loginId;
        memberService.getmemberInfo(loginId);
		System.out.println("원하는 기능의 번호를 입력해주세요.");
		System.out.println("1. 내 정보 수정");
		System.out.println("2. 회원탈퇴");
		System.out.println("3. 이전화면으로");
		int x = IOUtil.getInput(sc);
		switch(x) {
			case 1 -> {
				System.out.print("비밀번호를 한 번 더 입력해 주세요. : ");
				String password = IOUtil.getString(sc);
				if (checkPassword(loginId, password))
					updateMember(loginId);
				else {
					System.out.println("비밀번호가 일치하지 않습니다. 초기화면으로 돌아갑니다.");
				}
				}
			case 2 -> {
				id = deleteMember(loginId);
			}
			case 3 -> {}
		}
		return id;
    }
	

	private boolean checkPassword(int loginId, String password) {
		return memberService.checkPassword(loginId, password);
	}
	
	public void findId() {
		System.out.println("아이디 찾기");
		System.out.print("이름을 입력해 주세요 : ");
		String name = IOUtil.getString(sc);
		System.out.print("전화번호를 입력해 주세요 : ");
		String phone = IOUtil.getString(sc);
		String id = memberService.findId(name, phone);
		if (id == null) {
			System.out.println("가입하지 않았거나 입력하신 정보가 다릅니다.");
		}else {
			System.out.println("아이디 : " + id);
		}
    }

    public void findPassword() {
		System.out.println("비밀번호 찾기");
		System.out.print("아이디를 입력해 주세요 : ");
		String userId = IOUtil.getString(sc);
		String password = memberService.findPassword(userId);
		if (password == null) {
			System.out.println("가입하지 않은 아이디거나 입력하신 정보가 다릅니다.");
		}else {
			System.out.println("비밀번호 : " + password);
		}
    }

    public void updateMember(int loginId) {
		System.out.println("정보를 수정합니다.");
		System.out.println("변경할 아이디 : ");
		String username = IOUtil.getString(sc);
		System.out.println("변경할 비밀번호 : ");
		String password = IOUtil.getString(sc);
		System.out.println("변경할 이름 : ");
		String name = IOUtil.getString(sc);
		System.out.println("변경할 전화번호 : ");
		String phone = IOUtil.getString(sc);
		System.out.println("변경할 주소 : ");
		String address = IOUtil.getString(sc);
		
		System.out.println("원하는 기능의 번호를 입력해주세요 : ");
		System.out.println("1. 변경하기");
		System.out.println("2. 다시 입력");
		System.out.println("3. 이전 화면으로");
		int x = IOUtil.getInput(sc);
		switch(x) {
			case 1 -> {
				memberService.updateMember(loginId, username, password, name, phone, address);
				}
			case 2 -> updateMember(loginId);
			case 3 -> {}
		}
    }

    public void deleteMemberAdmin(int id) {
        memberService.deleteMember(id);
    }
    
    public int deleteMember(int id) {
    	memberService.deleteMember(id);
    	return -1;
    }

	public void logout(int loginId){
		memberService.logout(loginId);
	}
	
}

