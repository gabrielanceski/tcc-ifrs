package com.gabrielanceski.tccifrs.infrastructure.repository;

import com.gabrielanceski.tccifrs.domain.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, String> {

    @Query("SELECT t FROM Task t WHERE t.requirement.project.id = :projectId")
    List<Task> findAllByProjectId(String projectId);

    List<Task> findAllByRequirementId(String requirementId);
}
