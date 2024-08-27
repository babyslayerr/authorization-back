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

    public MemberRepository() {
        try {
            // H2 데이터베이스 드라이버를 메모리에 로드
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void selectMemberByUserIdAndMember(String userId, String password){
        // SQL 구문
        String sql = "SELECT id, userId, password FROM member";
        // DB 연결을 위한 연결 객체, url, username, password 를 파라미터로 사용
        // try 안의 괄호는 AutoableClosed 인터페이스를 구현한 객체로서 괄호안에 작성되게되면 try 구문 종료 후 자동으로 close() 메소드를 호출 한다
        try (Connection conn = DriverManager.getConnection(jdbcUrl,username,password);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Member member = new Member();
//                member.setId(rs.getLong("id"));
//                member.setName(rs.getString("name"));
//                member.setEmail(rs.getString("email"));
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public void updateMember(Long id, String name, String email) {
        String sql = "UPDATE members SET name = ?, email = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setLong(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM members WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}