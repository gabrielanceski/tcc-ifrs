package com.gabrielanceski.tccifrs.infrastructure.repository;

import com.gabrielanceski.tccifrs.domain.entity.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, String> {

    @NonNull List<Project> findAll();

}
