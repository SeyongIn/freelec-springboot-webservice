package com.insd.study.springboot.config.auth;

import com.insd.study.springboot.config.auth.dto.OAuthAttributes;
import com.insd.study.springboot.config.auth.dto.SessionUser;
import com.insd.study.springboot.domain.user.User;
import com.insd.study.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashSet;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        /* registrationId : 현재 로그인 진행 중인 서비스를 구분하는 코드
         * 구글 로그인인지, 네이버 로그인인지 구분하기 위해 사용됨
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        /* userNameAttributeName : OAuth2 로그인 진행 시 키가 되는 필드값. (Primary Key와 같은 의미)
         * 구글(기본코드:'sub'), 네이버/카카오(기본코드 없음)
         */
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuthAttributes : OAuth2UserService(인터페이스)를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(
                registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // OAuthAttributes의 최신값을 받았아서 저장되어 있는 사용자 정보(이름, 사진)를 업데이트 한다.
        User user = saveOrUpdate(attributes);

        /* SessionUser : 세션에 사용자 정보를 저장하기 위한 Dto
         * OAuth2User가 로그인에 성공하면, 세션에 user 정보를 저장하고 활용한다.
         */
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttribute(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())                   // 기존 이메일이 존재하는 경우
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());             // 기존 이메일이 존재하지 않는 경우 user에 소셜 사용자 정보 추가

        return userRepository.save(user);
    }
}
