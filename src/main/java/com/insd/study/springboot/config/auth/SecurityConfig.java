package com.insd.study.springboot.config.auth;

import com.insd.study.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity                                                      // Spring Security 설정들을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /* h2-console 화면을 사용하기 위해 해당 옵션들을 diable
                 * csrf(), header().frameOptions() 항목 체크하여 해당 옵션들 체크 :> OAuth2 마스터 책 참조 필요
                 */
                .csrf().disable().headers().frameOptions().disable()
                .and()
                    /* authorizeRequests : URL 별 권한 관리를 설정하는 옵션의 시작점
                     * authorizeRequests가 선언되어야만 antMatchers 옵션을 사용할 수 있음.
                     */
                    .authorizeRequests()
                        /* antMatchers : 권한 관리 대상을 지정하는 옵션
                         * URL, HTTP 메소드별로 관리가 가능함
                         */
                        // "/" 등 지정된 URL들에 대해서 전체 열람 권한을 준다.
                        .antMatchers("/", "/css/**", "/images/**", "/js/**", "h2-console/**").permitAll()
                        // "/api/v1/**" 주소를 가진 API는 USER 권한을 가진 사람만 가능하도록 한다.
                        .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    /* anyRequest : 위 설정된 값 외의 나머지 URL
                     * authenticated : 인증된(로그인한) 사용자에게만 허용
                     */
                    .anyRequest().authenticated()
                .and()
                    /* logout : 로그 아웃 기능에 대한 여러 설정 가능
                     * looutSuccessUrl : 로그 아웃 성공 시 "/" 주소로 이동
                     */
                    .logout().logoutSuccessUrl("/")
                .and()
                    /* oauthLogin : OAuth2 로그인 기능에 대한 여러 설정의 시작점
                     * userInfoEndpoint : OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정
                     * userService
                     */
                    .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);
    }
}
