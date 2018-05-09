package com.example.access.control.components.auth.service;

import com.example.access.control.components.auth.domain.SystemUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class SystemUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        String simpleUserName = "user";
        String superUserName = "superuser";

        if (userName != null) {
            SystemUserDetails result = null;

            if (userName.equals(simpleUserName)) {
                result = SystemUserDetails.builder()
                        .userName(simpleUserName)
                        .password("123")
                        .build();
            }
            else if (userName.equals(superUserName)) {
                Set<GrantedAuthority> authorities = new HashSet<>(Arrays.asList(
                        new SimpleGrantedAuthority("create_project"),
                        new SimpleGrantedAuthority("update_project"),
                        new SimpleGrantedAuthority("delete_project"),
                        new SimpleGrantedAuthority("create_person"),
                        new SimpleGrantedAuthority("update_person"),
                        new SimpleGrantedAuthority("delete_person")
                ));

                result = SystemUserDetails.builder()
                        .userName(superUserName)
                        .password("123")
                        .authorities(authorities)
                        .build();
            }

            if (result == null) {
                throw new UsernameNotFoundException(userName);
            }

            return result;
        }

        throw new UsernameNotFoundException("anonymous");
    }
}
