package com.gabrielanceski.tccifrs.application.service;

import com.gabrielanceski.tccifrs.domain.ProjectStatus;
import com.gabrielanceski.tccifrs.domain.Role;
import com.gabrielanceski.tccifrs.domain.entity.Company;
import com.gabrielanceski.tccifrs.domain.entity.Project;
import com.gabrielanceski.tccifrs.domain.entity.Team;
import com.gabrielanceski.tccifrs.domain.entity.User;
import com.gabrielanceski.tccifrs.domain.impl.AuthenticatedUser;
import com.gabrielanceski.tccifrs.infrastructure.repository.CompanyRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.ProjectRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.TeamRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.UserRepository;
import com.gabrielanceski.tccifrs.presentation.domain.request.ProjectCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.ProjectUpdateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.ProjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;
    private final TeamRepository teamRepository;

    /**
     * Cria um novo projeto
     * - Um projeto precisa estar associado a uma empresa.
     * - A empresa precisa estar previamente cadastrada no sistema.
     * - O status inicial do projeto é "CREATED".
     * - O projeto é criado sem equipes associadas.
     * @param request Request com os dados básicos para gerar um novo projeto.
     * @return Response com os dados do projeto criado.
     */
    public ProjectResponse createProject(ProjectCreateRequest request) {
        log.info("createProject() - creating new project - request <{}>", request);
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        User projectManager = userRepository.findById(request.projectManagerId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project Manager not found"));

        checkIfUserCanBeProjectManager(projectManager);

        Project project = new Project();
        project.setName(request.name());
        project.setStatus(ProjectStatus.CREATED);
        project.setDescription(request.description());
        project.setProjectManager(projectManager);
        project.setCompany(company);
        project.setTeams(Set.of());

        return ProjectResponse.fromEntity(projectRepository.save(project));
    }

    public List<ProjectResponse> listAll() {
        return projectRepository.findAll().stream()
            .map(ProjectResponse::fromEntity).toList();
    }

    public ProjectResponse getDetails(String id) {
        log.info("getDetails() - getting project details - projectId <{}>", id);
        return projectRepository.findById(id)
            .map(ProjectResponse::fromEntity)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    public ProjectResponse updateProject(String id, ProjectUpdateRequest request, AuthenticatedUser authenticatedUser) {
        log.info("updateProject() - updating project <{}> - request <{}>", id, request);

        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (!isAbleToUpdateProject(project, authenticatedUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this project.");
        }

        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStatus() != null) project.setStatus(ProjectStatus.fromString(request.getStatus()));
        if (request.getProjectManagerId() != null) {
            User projectManager = userRepository.findById(request.getProjectManagerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project Manager not found"));
            checkIfUserCanBeProjectManager(projectManager);
            project.setProjectManager(projectManager);
        }
        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
            project.setCompany(company);
        }
        if (request.getTeams() != null) {
            log.debug("updateProject() - changing teamList for project <{}>", id);
            Set<Team> teams = teamRepository.findTeamsByIdList(request.getTeams());
            project.setTeams(teams);
        }

        try {
            return ProjectResponse.fromEntity(projectRepository.save(project));
        } catch (Exception e) {
            log.error("updateProject() - error updating project data - projectId <{}>", id, e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error updating project data - projectId <" + id + ">");
        }
    }

    /**
     * Verifica se o usuário autenticado pode atualizar o projeto.
     * Para um usuário poder atualizar um projeto, ele precisa ser de nível ADMIN OU superior OU ser PROFESSOR, dono do projeto.
     * @param project Projeto a ser atualizado
     * @param authenticatedUser Usuário autenticado
     * @return true se o usuário pode atualizar o projeto, false caso contrário.
     */
    private boolean isAbleToUpdateProject(Project project, AuthenticatedUser authenticatedUser) {
        log.debug("isAbleToUpdateProject() - checking if user can update project - projectId <{}> | userId <{}> | role <{}>", project.getId(), authenticatedUser.getEntity().getId(), authenticatedUser.getEntity().getRole());
        if (Role.MASTER == authenticatedUser.getEntity().getRole() || Role.ADMIN == authenticatedUser.getEntity().getRole())
            return true;
        return project.getProjectManager().getId().equals(authenticatedUser.getEntity().getId());
    }

    private void checkIfUserCanBeProjectManager(User user) {
        log.debug("checkIfUserCanBeProjectManager() - checking if user can be a Project Manager - user <{}> | role <{}>", user.getId(), user.getRole());
        if (Role.PROFESSOR == user.getRole() || Role.ADMIN == user.getRole())
            return;
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This user is not allowed to be a Project Manager.");
    }
}
