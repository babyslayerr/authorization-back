package org.authorization.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.authorization.Member;
import org.authorization.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true") // 교차 출처 허용 + 쿠키나, 민감한 데이터 수용여부, 클라이언트의 withCredentials 옵션과 같이 활성화 되어야함, +보충설명) Credentials 이란 쿠키,Authorization 인증 헤더, client certificates(증명서)를 내포하는 자격 인증 정보를 말한다. //기본적으로 브라우저가 제공하는 요청 API 들은 별도의 옵션 없이 브라우저의 쿠키와 같은 인증과 관련된 데이터를 함부로 요청 데이터에 담지 않도록 되어있다. 이는 응답을 받을때도 마찬가지이다. 따라서 요청과 응답에 쿠키를 허용하고 싶을 경우, 이를 해결하기 위한 옵션이 바로 withCredentials 옵션이다. + 이는 ORIGIN이 다를 때(CORS) 해당하는 내용이다 + 서버도 해당옵션을 활성화 해야함
public class AuthenticationController {

    @Autowired
    private MemberService memberService;
    // login // 보안상 POST로 변경
    @PostMapping("/api/login") //springboot3.x 이후부터 매개변수 이름 명시
    public String getLogin(@RequestBody Member requestMember, HttpServletRequest request,HttpServletResponse response){ // @RequestBody 어노테이션은 자바 객체와 매핑됨 후에 reflection api 를 통해 필드주입됨, json 매핑에 사용되며, key값에 해당하는 필드가 있어야함
    //TODO DB -> PASSWORD, ID 검증, DTO 생성 필요
        // Optional<Member> member = memberService.login(userId,password);
        Optional<Member> member = Optional.of(requestMember);
        // 로그인된 멤버
        Member loginMember = member.orElseThrow();

        // 요청에 Session이 있으면 세션을 가져오고 없으면 새로 생성(후에 (세션)쿠키에 담겨짐
        HttpSession session = request.getSession();
        // 유지시간 설정(30분)
        session.setMaxInactiveInterval(1800);

        System.out.println("loginMember : " + loginMember.getUserId());
        System.out.println("sessionId : " + session.getId());
        // 세션에 로그인 정보 저장 -> 후에 HttpSessionListener같은 구현체에 아래 내용이 저장되고 SessionId는 헤더쿠키에 SET
        session.setAttribute("member",loginMember);

        return "success";
    }
    /*
    로그인 여부를 판단하는 메소드
    GET 요청이 세션이 들어있는 Cookie 를 가져올 수 없으므로 POST 로 변경
     */
    @GetMapping("/api/checkLogin")
    public Map<String, String> checkLogin(HttpServletRequest request,HttpServletResponse response){
        HttpSession session = request.getSession(false);// 만약 request에 session이 없으면 새로 생성할지 여부를 false

        // return 객체 초기화
        Map<String, String> returnMap = new HashMap<>();
        // 세션이 있으면 로그인
        if(session != null){
            // 키가 member인 value를 세션에서 가져옴
            Member member = (Member)session.getAttribute("member");
            // 로그인 여부와 userId를 리턴
            returnMap.put("isLogin", "true");
            returnMap.put("userId", member.getUserId());

        }else{
            // 로그인 여부를 false
            returnMap.put("isLogin", "false");
        }
        return returnMap;
    }

    /*
    로그아웃 api - 세션 비활성화
     */
    @GetMapping("/api/logout")
    public String getLogout(HttpServletRequest request){
        // servlet request 에서 session을 가져올 때 내용이 없으면 새롭게 생성하지 않기;
        HttpSession session = request.getSession(false);

        // null이 아니면(session이 있으면)
        if(session != null){
            // 세션 비활성화
            session.invalidate();
            return "성공적으로 로그아웃되었습니다.";
        }else{
            // 세션 만료시
            return "로그인 정보가 존재하지 않습니다.";
        }
    }
}
