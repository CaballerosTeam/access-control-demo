package com.example.access.control.components.project.repo;

import com.example.access.control.components.project.domain.Membership;
import org.springframework.data.repository.CrudRepository;

public interface MembershipRepository extends CrudRepository<Membership, Long> {

    Membership findByProjectIdAndPersonId(Long projectId, Long personId);
}
