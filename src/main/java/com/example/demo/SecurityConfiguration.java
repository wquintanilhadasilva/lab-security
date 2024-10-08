package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
public class SecurityConfiguration {


    @Bean
    protected SecurityFilterChain configure(final HttpSecurity http, JwtDecoder delegatingJwtDecoder,
                                            AuthenticationEntryPoint authenticationEntryPoint) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(conf ->
                        conf
                            .antMatchers("/public/**").permitAll()
                            .anyRequest().authenticated()
                )
                .oauth2ResourceServer(resource -> resource
                        .jwt(jwt -> jwt.decoder(delegatingJwtDecoder))
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .build();
    }

    @Bean
    public TokenResolver tokenResolver() {
        return new DefaultTokenResolver("9SyECk96oDsTmXfogIieDI0cD/8FpnojlYSUJT5U9I/FGVmBz5oskmjOR8cbXTvoPjX+Pq/T/b1PqpHX0lYm0oCBjXWICA==");
    }

    @Bean
    public JwtDecoder delegatingJwtDecoder(TokenResolver tokenResolver) {
        return new OobjRestApiJwtDecoder(tokenResolver);
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPointHandler();
    }

//    @Bean
//    public AccessDeniedHandler customAccessDeniedHandler() {
//        return new CustomAccessDeniedHandler();
//    }

}
