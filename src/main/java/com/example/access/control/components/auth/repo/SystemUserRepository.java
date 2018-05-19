package com.example.access.control.components.auth.repo;

import com.example.access.control.components.auth.domain.SystemUser;
import org.springframework.data.repository.CrudRepository;

public interface SystemUserRepository extends CrudRepository<SystemUser, Long> {

    SystemUser getSystemUserWithAuthoritiesByUserName(String userName);
}
