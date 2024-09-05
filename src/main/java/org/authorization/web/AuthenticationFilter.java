package org.authorization.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;



// WAS 서블릿 사이에 수행되는 Filter 정의
public class AuthenticationFilter implements Filter {

    private static final String[] excludeUrlPatterns = {"/", "/api/login", "/api/signUpMember"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // httpServlet 사용을 위한 다운 캐스팅
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        try {
            // 로그인 패스 경로가 아니면 조건문 안의 인증 로직 실행
            if (!isLoginPassPath(requestURI)) {
                System.out.println("인증 시작");
                // request 에서 세션 가져오기
                HttpSession session = httpRequest.getSession(false);// 세션이 없으면 새로운 세션 생성x
                // 세션이 없거나 member에 해당하는 내용이 없으면 미인증자
                if (session == null || session.getAttribute("member") == null) {
                    System.out.println("미인증 사용자 요청");
                    return; // 미인증 사용자는 함수 리턴
                }
            }
            // 이후 다음 필터 실행
            chain.doFilter(request, response);
        }catch (Exception e){
            throw e; // 톰캣으로 예외 던짐
        }finally {
            System.out.println("인증 필터 종료");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    // 제외할 URL 패턴 일치 여부 확인 - 제외 URL 일치시 false 리턴
    private boolean isLoginPassPath(String requestURI){
        return PatternMatchUtils.simpleMatch(excludeUrlPatterns, requestURI);
    }

}
