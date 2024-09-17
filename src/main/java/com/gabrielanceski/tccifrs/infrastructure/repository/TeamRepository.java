package com.gabrielanceski.tccifrs.infrastructure.repository;

import com.gabrielanceski.tccifrs.domain.entity.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TeamRepository extends CrudRepository<Team, String> {
    Team findByName(String name);

    @Query("SELECT t FROM Team t WHERE t.leader.id = :id")
    Set<Team> findTeamsByLeaderId(String id);

    @Query("SELECT t FROM Team t JOIN t.members m WHERE m.id = :id OR t.leader.id = :id")
    Set<Team> findTeamsByUserId(String id);
}
