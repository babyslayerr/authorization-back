package org.authorization.repository;

import org.authorization.Member;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MemberRepository {
    private String jdbcUrl = "jdbc:h2:~/testAuth"; // H2 인메모리 데이터베이스 URL
    private String username = "sa"; // H2 기본 사용자 이름
    private String password = ""; // H2 기본 비밀번호

    /*
    생성자 생성시, H2 DB연결 을 위한 드라이버 메모리에 로드
     */
    public MemberRepository() {
        try {
            // H2 데이터베이스 드라이버를 메모리에 로드
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    * 유저와 비밀번호를 이용해서 멤버(로그인 회원)찾기
    * */
    public Member selectMemberByUserIdAndMember(String userId, String password){
        // return값 초기회
        Member member = null;
        // SQL 구문
        String sql = "SELECT id, user_Id, password FROM member WHERE USER_ID = (?) AND PASSWORD = (?)";
        // DB 연결을 위한 연결 객체, url, username, password 를 파라미터로 사용
        // try 안의 괄호는 AutoableClosed 인터페이스를 구현한 객체로서 괄호안에 작성되게되면 try 구문 종료 후 자동으로 close() 메소드를 호출 한다
        try (Connection conn = DriverManager.getConnection(this.jdbcUrl,this.username,this.password);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ){
            // 파라미터 세팅
            pstmt.setString(1,userId);
            pstmt.setString(2,password);
            // 실행 후 (단일)값이 있으면 Member에 set
            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next()){
                // return 객체 생성
                member = new Member();
                // 속성값 기입
                member.setId(resultSet.getLong("id"));
                member.setUserId(resultSet.getString("user_id"));
                member.setPassword(resultSet.getString("password"));
            }
        }
         catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return member;
    }

    public void createMember(String name, String email) {
        String sql = "INSERT INTO MEMBER (name, email) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             // 미리 컴파일된 SQL 구문을 나타내는 객체
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






}