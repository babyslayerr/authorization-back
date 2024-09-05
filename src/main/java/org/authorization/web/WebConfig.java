package org.authorization.web;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 스프링부트 부트 시 내부 WAS(서블릿 컨테이너) 의 필터 등록에 필요한 빈 설정 클래스
// 빈 등록 클래스
@Configuration
public class WebConfig {


    @Bean // 로그 필터 등록
    public FilterRegistrationBean<Filter> logFilter(){
        // 필터 등록에 필요한 빈 객체 생성
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean();
        // 필터 등록
        filterRegistrationBean.setFilter(new LogFilter());
        // 필터 순서
        filterRegistrationBean.setOrder(1);
        // 필터 URL 적용 범위 ( 현재 모든 URL 적용)
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean // 인증 필터 등록
    public FilterRegistrationBean<Filter> authenticationFilter(){

        // 필터 등록에 필요한 빈 객체 생성
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean();
        // 인증 필터 등록
        filterRegistrationBean.setFilter(new AuthenticationFilter());
        // 인증 필터 순서
        filterRegistrationBean.setOrder(2);
        // URL 적용 범위
        filterRegistrationBean.addUrlPatterns("/*");
        // 필터 제외 url 지정 (메인, 로그인, 회원가입 페이지)
        filterRegistrationBean.addInitParameter("excludeUrlPatterns", "/, /api/login, /api/signUpMember");

        return filterRegistrationBean;
    }

}
