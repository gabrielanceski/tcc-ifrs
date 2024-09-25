package com.gabrielanceski.tccifrs.application.service;

import com.gabrielanceski.tccifrs.domain.ProjectStatus;
import com.gabrielanceski.tccifrs.domain.entity.Company;
import com.gabrielanceski.tccifrs.domain.entity.Project;
import com.gabrielanceski.tccifrs.domain.entity.Team;
import com.gabrielanceski.tccifrs.infrastructure.repository.CompanyRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.ProjectRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.TeamRepository;
import com.gabrielanceski.tccifrs.presentation.domain.request.ProjectCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.ProjectUpdateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.ProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectService {
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
    public ProjectResponse create(ProjectCreateRequest request) {
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));

        Project project = new Project();
        project.setName(request.name());
        project.setStatus(ProjectStatus.CREATED);
        project.setDescription(request.description());
        project.setCompany(company);
        project.setTeams(Set.of());

        return ProjectResponse.fromEntity(projectRepository.save(project));
    }

    public List<ProjectResponse> listAll() {
        return projectRepository.findAll().stream()
            .map(ProjectResponse::fromEntity).toList();
    }

    public ProjectResponse getDetails(String id) {
        return projectRepository.findById(id)
            .map(ProjectResponse::fromEntity)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    public ProjectResponse update(String id, ProjectUpdateRequest request) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStatus() != null) project.setStatus(ProjectStatus.fromString(request.getStatus()));
        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
            project.setCompany(company);
        }
        if (request.getTeams() != null && !request.getTeams().isEmpty()) {
            Set<Team> teams = teamRepository.findTeamsByIdList(request.getTeams());
            project.setTeams(teams);
        }

        try {
            return ProjectResponse.fromEntity(projectRepository.save(project));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error updating project data - projectId <" + id + ">");
        }
    }
}
