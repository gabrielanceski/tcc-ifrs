package com.gabrielanceski.tccifrs.infrastructure.repository;

import com.gabrielanceski.tccifrs.domain.entity.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<UserRole, Long> {
    Optional<UserRole> findByRole(String roleName);
}
