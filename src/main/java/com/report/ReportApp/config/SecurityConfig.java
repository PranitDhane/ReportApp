package com.report.ReportApp.config;

import com.report.ReportApp.security.JWTAuthenticationEntryPoint;
import com.report.ReportApp.security.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JWTAuthenticationEntryPoint entryPoint;
    @Autowired
    private JWTAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.csrf(csrf->csrf.disable())
                .cors(cors->cors.disable())
                .authorizeHttpRequests(auth-> auth.requestMatchers("/home/**").hasRole("ADMIN").requestMatchers("/user/**").hasRole("ADMIN")
                        .requestMatchers("/auth/login").permitAll().anyRequest().authenticated())
                .exceptionHandling(ex->ex.authenticationEntryPoint(entryPoint))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class);
        return  http.build();
    }
}
