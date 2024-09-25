package com.gabrielanceski.tccifrs.application.service;

import com.gabrielanceski.tccifrs.domain.Role;
import com.gabrielanceski.tccifrs.domain.entity.Team;
import com.gabrielanceski.tccifrs.domain.entity.User;
import com.gabrielanceski.tccifrs.exception.UserNotFoundException;
import com.gabrielanceski.tccifrs.infrastructure.repository.TeamRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.UserRepository;
import com.gabrielanceski.tccifrs.presentation.domain.request.TeamCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.TeamUpdateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.TeamResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamResponse createTeam(TeamCreateRequest request) {
        log.info("createTeam() - Creating team request: <{}>", request);

        User leader = userRepository.findById(request.leaderId())
            .orElseThrow(() -> new IllegalArgumentException("Leader id <" + request.leaderId() + "> not found"));
        Set<User> members = findMembers(request.members());

        checkIfUserHasAssociableRole(leader);
        members.forEach(this::checkIfUserHasAssociableRole);

        Team team = new Team();
        team.setName(request.name());
        team.setLeader(leader);
        team.setMembers(members);
        team.setProjects(Set.of());

        // Associa o líder da equipe à lista de membros
        return associateUserToTeam(leader.getId(), teamRepository.save(team).getId());
    }

    /**
     * Associa um usuário à uma equipe
     * @param userId Id do usuário
     * @param teamId Id da equipe
     * @return Detalhes da equipe
     */
    public TeamResponse associateUserToTeam(String userId, String teamId) {
        log.info("associateUserToTeam() - Associating user <{}> to team <{}>", userId, teamId);

        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new IllegalArgumentException("Team id <" + teamId + "> not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        checkIfUserHasAssociableRole(user);

        List<String> teamMembers = teamRepository.findTeamMemberIds(teamId);

        log.info("Team members: {}", teamMembers);

        if (teamMembers.contains(user.getId())) {
            log.info("associateUserToTeam() - User {} is already associated to team {}", user.getId(), team.getId());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User id <" + user.getId() + "> is already associated to team id <" + team.getId() + ">");
        }

        log.info("associateUserToTeam() - User <{}> associated to team <{}>", userId, teamId);
        team.getMembers().add(user);

        return TeamResponse.fromEntity(teamRepository.save(team));
    }

    /**
     * Promove um usuário à líder de uma equipe.
     * Para que um usuário possa ser promovido à líder, ele precisa ser de uma role inferior a MASTER.
     * O usuário que será promovido precisa estar associado a equipe.
     * O novo líder não pode ser igual ao líder atual.
     * O antigo líder permanecerá na lista de membros da equipe.
     * @param userId Id do usuário membro de equipe.
     * @param teamId Id da equipe.
     * @return Detalhes da equipe após alterações.
     */
    public TeamResponse promoteUserAsLeader(String userId, String teamId) {
        log.info("promoteUserToLeader() - Promoting user <{}> to leader of team <{}>", userId, teamId);

        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new IllegalArgumentException("Team id <" + teamId + "> not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        checkIfUserHasAssociableRole(user);

        if (team.getLeader().getId().equalsIgnoreCase(userId)) {
            log.error("promoteUserToLeader() - User {} is already the leader of team {}", userId, teamId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User id <" + userId + "> is already the leader of team id <" + teamId + ">");
        }

        if (!team.getMembers().contains(user)) {
            log.error("promoteUserToLeader() - User {} is not associated to team {}", userId, teamId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User id <" + user.getId() + "> is not associated to team id <" + team.getId() + ">");
        }

        team.setLeader(user);
        log.info("promoteUserToLeader() - User <{}> is now the leader of team <{}>. The old leader still on the member list.", userId, teamId);

        return TeamResponse.fromEntity(teamRepository.save(team));
    }

    /**
     * Remove um membro de uma equipe.
     * Se o membro for o líder da equipe, ele não poderá ser removido da lista de membros e nem da liderança através desse método.
     * Um membro precisa estar associado à equipe para ser removido.
     * @param userId Membro a ser removido.
     * @param teamId Equipe.
     * @return Detalhes da equipe após alterações.
     */
    public TeamResponse removeMemberFromTeam(String userId, String teamId) {
        log.info("removeMemberFromTeam() - Removing user <{}> from team <{}>", userId, teamId);

        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new IllegalArgumentException("Team id <" + teamId + "> not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        if (team.getLeader().getId().equalsIgnoreCase(userId)) {
            log.error("removeMemberFromTeam() - User {} is the leader of team {}. Promote another user as leader before trying to remove user from team's member list.", userId, teamId);
            throw new IllegalArgumentException("User id <" + userId + "> is the leader of team id <" + teamId + ">");
        }

        if (!team.getMembers().contains(user)) {
            log.error("removeMemberFromTeam() - User {} is not associated to team {}", userId, teamId);
            throw new IllegalArgumentException("User id <" + user.getId() + "> is not associated to team id <" + team.getId() + ">");
        }

        team.getMembers().remove(user);
        log.info("removeMemberFromTeam() - User <{}> removed from team <{}>", userId, teamId);

        return TeamResponse.fromEntity(teamRepository.save(team));
    }

    private Set<User> findMembers(Set<String> members) {
        return members.stream()
            .map(member -> userRepository.findById(member)
                .orElseThrow(() -> new UserNotFoundException(member)))
            .collect(Collectors.toSet());
    }

    /**
     * Checa se um usuário possui uma role associável a uma equipe.
     * Usuários de Role MASTER não podem ser associados a equipes.
     * @param user Usuário.
     */
    private void checkIfUserHasAssociableRole(User user) {
        log.debug("checkIfUserHasAssociableRole() - Checking if user <{}> can be associated to a team", user.getId());
        if (Role.MASTER == user.getRole()) {
            throw new IllegalArgumentException("User id <" + user.getId() + "> cannot be associated to a team");
        }
    }

    public List<TeamResponse> listTeams() {
        log.info("listTeams() - Listing all teams");
        return teamRepository.findAll().stream()
            .map(TeamResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public TeamResponse getTeamDetails(String teamId) {
        log.info("getTeamDetails() - Getting team details for team <{}>", teamId);
        return TeamResponse.fromEntity(teamRepository.findById(teamId)
            .orElseThrow(() -> new IllegalArgumentException("Team id <" + teamId + "> not found")));
    }

    public TeamResponse updateTeam(TeamUpdateRequest request) {
        log.info("updateTeam() - Updating team with request: <{}>", request);

        Team team = teamRepository.findById(request.id())
            .orElseThrow(() -> new IllegalArgumentException("Team id <" + request.id() + "> not found"));

        User leader = userRepository.findById(request.leaderId())
            .orElseThrow(() -> new IllegalArgumentException("Leader id <" + request.leaderId() + "> not found"));
        Set<User> members = findMembers(request.members());

        checkIfUserHasAssociableRole(leader);
        members.forEach(this::checkIfUserHasAssociableRole);

        team.setName(request.name());
        team.setLeader(leader);
        team.setMembers(members);

        return TeamResponse.fromEntity(teamRepository.save(team));
    }
}
