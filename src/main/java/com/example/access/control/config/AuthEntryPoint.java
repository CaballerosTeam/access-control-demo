package com.example.access.control.config;

import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("Sveltlyachok");
        super.afterPropertiesSet();
    }
}
