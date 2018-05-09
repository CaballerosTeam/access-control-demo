package com.example.access.control.config;

import com.example.access.control.components.auth.service.SystemUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BasicAuthenticationEntryPoint basicAuthenticationEntryPoint;
    private final SystemUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(BasicAuthenticationEntryPoint basicAuthenticationEntryPoint,
                          SystemUserDetailsService userDetailsService) {

        this.basicAuthenticationEntryPoint = basicAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {

        AuthenticationProvider authenticationProvider = getAuthenticationProvider();
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(basicAuthenticationEntryPoint);
    }

    @Bean
    AuthenticationProvider getAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);

        PasswordEncoder passwordEncoder = getPasswordEncoder();
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        class PlainTextPasswordEncoder implements PasswordEncoder {
            @Override
            public String encode(CharSequence rawPassword) {

                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {

                String rawPasswordString = rawPassword.toString();

                return rawPasswordString.equals(encodedPassword);
            }
        }

        return new PlainTextPasswordEncoder();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("{noop}123").roles("USER");
//    }
}
