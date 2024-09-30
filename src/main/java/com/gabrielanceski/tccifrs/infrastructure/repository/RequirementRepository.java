package com.gabrielanceski.tccifrs.infrastructure.repository;

import com.gabrielanceski.tccifrs.domain.entity.Requirement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRepository extends CrudRepository<Requirement, String> {
    List<Requirement> findAllByProjectId(String projectId);
}
