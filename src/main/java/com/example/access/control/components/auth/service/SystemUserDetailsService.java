package com.example.access.control.components.auth.service;

import com.example.access.control.components.auth.domain.SystemUser;
import com.example.access.control.components.auth.repo.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SystemUserDetailsService implements UserDetailsService {

    private final SystemUserRepository repository;

    @Autowired
    public SystemUserDetailsService(SystemUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SystemUser user = repository.getSystemUserWithAuthoritiesByUserName(userName);

        if (user == null) throw new UsernameNotFoundException(userName);

        return user;
    }

    public SystemUser create(SystemUser user) {

        return repository.save(user);
    }
}
