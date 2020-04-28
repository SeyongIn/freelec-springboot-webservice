package com.insd.study.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/* @SpringBootApplication : 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성을 모두 자동으로 설정
 * @SpringBootApplication이 있는 위치부터 설정을 읽어 들이기 때문에 프로젝트 최상단에 위치해야 함.
 */
@SpringBootApplication
//@EnableJpaAuditing                    // JPS Auditing 활성화 (BaseTimeEntity 클래스)  >>  별도 클래스(JpaConfig)로 분리
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}