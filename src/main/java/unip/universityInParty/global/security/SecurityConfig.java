package unip.universityInParty.global.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import unip.universityInParty.global.oauth2.service.CustomOAuthUserService;
import unip.universityInParty.global.security.custom.CustomSuccessHandler;
import unip.universityInParty.global.security.jwt.JwtFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler; // 인증 성공 시 처리할 커스텀 핸들러
    private final CustomOAuthUserService customOAuthUserService; // OAuth2 사용자 정보 서비스
    private final JwtFilter jwtFilter; // JWT 필터

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CORS 설정 시작
        http
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration configuration = new CorsConfiguration();
                    // 허용할 출처(Origin)를 설정합니다.
                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));

                    // 허용할 HTTP 메서드를 설정합니다.
                    configuration.setAllowedMethods(Collections.singletonList("*"));

                    // 자격 증명(Credentials)을 포함한 요청을 허용합니다.
                    configuration.setAllowCredentials(true);

                    // 허용할 요청 헤더를 설정합니다.
                    configuration.setAllowedHeaders(Collections.singletonList("*"));

                    // CORS 요청의 유효 기간을 설정합니다.
                    configuration.setMaxAge(3600L);

                    // 클라이언트에게 노출할 헤더를 설정합니다.
                    configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "access"));

                    return configuration;
                }
            }));

        // CSRF 보호 비활성화
        http
            .csrf(auth -> auth.disable());

        // Form 기반 로그인 비활성화
        http
            .formLogin(auth -> auth.disable());

        // HTTP Basic 인증 비활성화
        http
            .httpBasic(auth -> auth.disable());

        // JWT 필터 추가
        http
            .addFilterAfter(jwtFilter, OAuth2LoginAuthenticationFilter.class);

        // OAuth2 로그인 설정
        http
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                    .userService(customOAuthUserService))
                .successHandler(customSuccessHandler) // 커스텀 성공 핸들러 설정
            );

        // 요청 권한 설정
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login/oauth2/code/**", "/refresh", "/api/**").permitAll() // 로그인 및 리프레시 경로를 인증 없이 접근 허용
                .anyRequest().authenticated()); // 나머지 모든 요청은 인증된 사용자만 접근 허용

        // 세션 관리 설정: STATELESS
        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}