package com.gabrielanceski.tccifrs.application.service;

import com.gabrielanceski.tccifrs.domain.Constants;
import com.gabrielanceski.tccifrs.domain.entity.Team;
import com.gabrielanceski.tccifrs.domain.entity.User;
import com.gabrielanceski.tccifrs.exception.UserNotFoundException;
import com.gabrielanceski.tccifrs.infrastructure.repository.TeamRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.UserRepository;
import com.gabrielanceski.tccifrs.presentation.domain.request.TeamCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.TeamResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public record TeamService(TeamRepository teamRepository, UserRepository userRepository) {

    public TeamResponse createTeam(TeamCreateRequest request) {
        log.info("createTeam() - Creating team request: <{}>", request);

        User leader = userRepository.findById(request.leaderId())
            .orElseThrow(() -> new IllegalArgumentException("Leader id <" + request.leaderId() + "> not found"));
        Set<User> members = findMembers(request.members());

        checkIfUserCanBeAssociatedToTeam(leader);
        members.forEach(this::checkIfUserCanBeAssociatedToTeam);

        Team team = new Team();
        team.setName(request.name());
        team.setLeader(leader);
        team.setMembers(members);
        team.setProjects(Set.of());

        // Associa o líder da equipe à lista de membros
        return associateUserToTeam(teamRepository.save(team).getId(), leader.getId());
    }

    /**
     * Associa um usuário à uma equipe
     * @param teamId Id da equipe
     * @param userId Id do usuário
     * @return Detalhes da equipe
     */
    public TeamResponse associateUserToTeam(String teamId, String userId) {
        log.info("associateUserToTeam() - Associating user <{}> to team <{}>", userId, teamId);

        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new IllegalArgumentException("Team id <" + teamId + "> not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        checkIfUserCanBeAssociatedToTeam(user);

        team.getMembers().add(user);

        return TeamResponse.fromEntity(teamRepository.save(team));
    }

    private Set<User> findMembers(Set<String> members) {
        return members.stream()
            .map(member -> userRepository.findById(member)
                .orElseThrow(() -> new UserNotFoundException(member)))
            .collect(Collectors.toSet());
    }

    private void checkIfUserCanBeAssociatedToTeam(User user) {
        log.debug("checkIfUserCanBeAssociatedToTeam() - Checking if user <{}> can be associated to a team", user.getId());
        if (Constants.MASTER_ROLE.equalsIgnoreCase(user.getRole().getRole())) {
            throw new IllegalArgumentException("User id <" + user.getId() + "> cannot be associated to a team");
        }
    }

}
