package com.insd.study.springboot.config.auth.dto;

import com.insd.study.springboot.domain.user.Role;
import com.insd.study.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attribute;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attribute,
                          String nameAttributeKey, String name, String email, String picture) {
        this.attribute = attribute;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName, Map<String, Object> attributes) {

        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        /* 네이버 오픈 API의 회원 로그인 결과 (JSON)
            {
                "resultcode":"00",
                "message":"success",
                "response":{
                    "email":"openapi@naver.com",
                    "nickname":"OpenAPI",
                    "profile_image":"https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif",
                    "age":"40-49",
                    "gender":"F",
                    "id":"32742776",
                    "name":"오픈 API",
                    "birthday":"10-01"
            }
         */

        // "response"의 하위 속성를 Map으로 가져온다.
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");

        return OAuthAttributes.builder()
                .name((String)response.get("name"))
                .email((String)response.get("email"))
                .picture((String)response.get("profile_image"))
                .attribute(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .picture((String)attributes.get("picture"))
                .attribute(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 소셜 로그인한 사용자가 기존에 가입되어 있지 않은 경우 호출됨.
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)                   // 신규 사용자의 Role은 GUEST
                .build();
    }
}
