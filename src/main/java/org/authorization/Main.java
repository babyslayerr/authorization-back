package org.authorization;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 컴포넌트 스캔 범위 기준점
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class); // 메인클래스 지정
    }
}