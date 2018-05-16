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
        String createUserName = "createUser";
        String updateUserName = "updateUser";
        String deleteUserName = "deleteUser";
        String password = "123";

        if (userName != null) {
            SystemUserDetails result = null;

            if (userName.equals(simpleUserName)) {
                result = SystemUserDetails.builder()
                        .userName(simpleUserName)
                        .password(password)
                        .build();
            }
            else if (userName.equals(createUserName)) {
                Set<GrantedAuthority> authorities = new HashSet<>(Arrays.asList(
                        new SimpleGrantedAuthority("create_project"),
                        new SimpleGrantedAuthority("create_person")
                ));

                result = SystemUserDetails.builder()
                        .userName(createUserName)
                        .password(password)
                        .authorities(authorities)
                        .build();
            }
            else if (userName.equals(updateUserName)) {
                Set<GrantedAuthority> authorities = new HashSet<>(Arrays.asList(
                        new SimpleGrantedAuthority("update_project"),
                        new SimpleGrantedAuthority("update_person")
                ));

                result = SystemUserDetails.builder()
                        .userName(updateUserName)
                        .password(password)
                        .authorities(authorities)
                        .build();
            }
            else if (userName.equals(deleteUserName)) {
                Set<GrantedAuthority> authorities = new HashSet<>(Arrays.asList(
                        new SimpleGrantedAuthority("delete_project"),
                        new SimpleGrantedAuthority("delete_person")
                ));

                result = SystemUserDetails.builder()
                        .userName(deleteUserName)
                        .password(password)
                        .authorities(authorities)
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
                        .password(password)
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
