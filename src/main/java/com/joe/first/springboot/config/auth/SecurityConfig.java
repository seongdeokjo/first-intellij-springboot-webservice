package com.joe.first.springboot.config.auth;

import com.joe.first.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // spring security 설정들을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 해당 옵션 disable
                .and()
                    .authorizeRequests()  // URL 별 권한 관리를 설정하는 옵션의 시작점 / 메소드가 선언되어야만 antMatchers 옵션 사용가능
                    .antMatchers("/","/css/**","/images/**","/js/**","/h2-console/**").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // USER 권한을 가진 사람만 가능하도록
                    .anyRequest().authenticated() // 설정된 값들 이외 나머지 URL들을 나타냄 ,  authenticated() 나머지 URL 들은 모두 인증된 사용자들에게만 허용
                                                // 인증된 사용자 즉, 로그인한 사용자들을 이야기함
                .and()
                    .logout()  // logout 기능에 대한 여러 설정의 진입점 로그아웃 성공시 /주소로 이동
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint() // oauth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                            .userService(customOAuth2UserService); // 로그인 성공시 후속 조치 진행할 인터페이스의 구현제 등록
                                                                    // 리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시
    }
}
