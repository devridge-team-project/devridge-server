package org.devridge.api.common.security.config;

import lombok.RequiredArgsConstructor;
import org.devridge.api.common.security.auth.CustomAuthenticationEntryPoint;
import org.devridge.api.common.security.constant.SecurityConstant;
import org.devridge.api.common.security.filter.JwtAuthenticationFilter;
import org.devridge.api.common.security.filter.JwtAuthorizationFilter;
import org.devridge.api.common.security.handler.CustomLogoutHandler;
import org.devridge.api.domain.auth.repository.RefreshTokenRepository;
import org.devridge.api.domain.member.repository.MemberRepository;
import org.devridge.api.domain.skill.repository.MemberSkillRepository;
import org.devridge.api.common.security.auth.CustomMemberDetailsService;
import org.devridge.api.common.security.auth.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomMemberDetailsService customMemberDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final MemberSkillRepository memberSkillRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationConfiguration authConfig = http.getSharedObject(AuthenticationConfiguration.class);
        AuthenticationManager authenticationManager = authenticationManager(authConfig);

        JwtAuthenticationFilter jwtAuthenticationFilter = jwtAuthenticationFilter(authenticationManager);
        JwtAuthorizationFilter jwtAuthorizationFilter = jwtAuthorizationFilter(authenticationManager);

        http
            .cors();

        http.exceptionHandling()
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);    // jwt 사용으로 세션관리 해제

        http
            .headers().frameOptions().sameOrigin();

        http
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessHandler(new CustomLogoutHandler(refreshTokenRepository));

        http
            .authorizeRequests()
            .antMatchers(securityConstant().ALL_PERMIT_PATHS).permitAll()
            .antMatchers(HttpMethod.GET, "/api/hashtags/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/qna/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/occupations").permitAll()
            .antMatchers(HttpMethod.GET, "/api/community/**").permitAll()
            .antMatchers(HttpMethod.PATCH, "/api/users/reset-password").permitAll()
            .antMatchers("/api/ws").permitAll()
            .antMatchers("/api/qna/**").authenticated()
            .antMatchers("/api/community/**").authenticated()
            .antMatchers(securityConstant().USER_ROLE_PERMIT_PATHS).hasRole(SecurityConstant.USER_ROLE)
            .anyRequest().denyAll();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public SecurityConstant securityConstant() {
        return new SecurityConstant();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomLogoutHandler customLogoutHandler(){
        return new CustomLogoutHandler(refreshTokenRepository);
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() throws Exception {
        return new JwtAuthenticationProvider(customMemberDetailsService, passwordEncoder());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(refreshTokenRepository, memberSkillRepository);
        authenticationFilter.setAuthenticationManager(authenticationManager);

        SecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
        authenticationFilter.setSecurityContextRepository(contextRepository);

        return authenticationFilter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        JwtAuthorizationFilter authorizationFilter = new JwtAuthorizationFilter(authenticationManager, memberRepository, refreshTokenRepository);
        return authorizationFilter;
    }
}
