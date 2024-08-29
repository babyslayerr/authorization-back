package org.authorization;

// 회원 여부를 결정 하는 클래스
public class Member {
    private long id = 1;
    private String userId;
    private String password;

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
