package org.authorization.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/*
* 서블릿에서 제공하는 HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 컨트롤러 (와스 서블릿 사이에 실행되는 메소드)
* 싱클톤 인스턴스
* */
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("log filter init");
    }

    /*
    HTTP 요청 후 서블릿이 호출되기전 실행되는 메소드
    실제 로그 남길 때, 후에 logback mdc 적용 - 하나의 요청에 대한 로그에 동일한 식별자 공유
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 현재 타임스탬프 가져오기
        String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        // 로그 레벨
        String logLevel = "INFO"; // 또는 다른 로그 레벨을 동적으로 설정할 수 있습니다.

        // 프로세스 ID 가져오기
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        String processId = String.valueOf(runtimeMXBean.getName().split("@")[0]);

        // 현재 스레드 이름 가져오기
        String threadName = Thread.currentThread().getName();

        // 클래스 이름 (현재 클래스)
        String className = LogFilter.class.getName();

        // 메시지 (예시)
        String message = "애플리케이션이 시작되었습니다.";

        // 로그 메시지 생성
        String logMessage = String.format("%s  %s %s --- %s %s : %s",
                timestamp, logLevel, processId, threadName, className, message);



        System.out.println("log filter doFilter");
        System.out.println(logMessage);

        // 접속 URL 로그 출력
        // 다운캐스팅 , request 는 http 요청이 아닌 경우까지 고려하였기 때문
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        // 사용자 구분을 위한 UUID 작성
        String uuid = UUID.randomUUID().toString();
        try{
            // 로그 남기기, 요청 uuid 포함
            System.out.printf("REQUEST %s %s \n", requestURI, uuid);
            // 다음 필터 호출(있으면 다음 필터를 호출 하고, 없으면 서블릿을 호출) 이 로직을 실행해야만 다음 단계로 진행함
            chain.doFilter(request,response);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.printf("RESPONSE %s %s \n", requestURI, uuid);
        }
    }

    @Override
    public void destroy() {
        System.out.println("log filter destroy");
    }
}
