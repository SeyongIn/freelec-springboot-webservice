package com.insd.study.springboot.domain.user;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);       // 소셜 로그인 반환값 중 email을 통해 사용자 가입여부를 체크하기 위한 메소드
}
