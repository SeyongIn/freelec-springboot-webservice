package com.insd.study.springboot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)                  // 메소드의 파라미터로 선언된 객체에만 사용 가능
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}
