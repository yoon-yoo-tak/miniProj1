# Tasks

## 프로젝트 구성
- [x] 프로젝트 생성
- [x] ERD 설계 
- [x] DB 테이블 생성  
    - [x] Member
    - [x] Board
    - [x] LoginHistory

## 기능 구현

### Member
- [x] 회원가입
- [x] 로그인 / 로그아웃
  - [x] LoginHistory, Member 기록 (procedure)
- [x] 회원정보 조회
- [x] 회원정보 수정
- [x] 아이디 찾기 (이름/전화번호만 체크)
- [x] 비밀번호 찾기 (아이디로만 체크)
- [x] 회원 탈퇴 


### Board
- [x] 글 목록
  - [x] 페이징
  - [x] 페이지 이동
    - [x] 이전 페이지로
    - [x] 다음 페이지로
    - [x] 특정 페이지로
- [x] 글 조회
  - [x] 조회수 증가
  - [x] 수정(작성자일경우)
  - [x] 삭제(작성자일경우)
- [x] 글 쓰기



### Admin
- [x] 모든 유저 목록
  - [x] 삭제 기능
- [x] 글 목록
  - [x] 삭제 기능

### 추가기능 / 리팩토링
- [ ] Validation Check
- [ ] 클래스 분리
- [ ] 상황별 예외메시지 처리
- [ ] 하드코딩 정리
  

# ERD diagram
![ERD](https://github.com/yoon-yoo-tak/miniProj1/blob/master/img/erd1.JPG?raw=true)