package com.gabrielanceski.tccifrs.application.service;

import com.gabrielanceski.tccifrs.domain.RequirementLevel;
import com.gabrielanceski.tccifrs.domain.entity.Project;
import com.gabrielanceski.tccifrs.domain.entity.Requirement;
import com.gabrielanceski.tccifrs.domain.impl.AuthenticatedUser;
import com.gabrielanceski.tccifrs.infrastructure.repository.ProjectRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.RequirementRepository;
import com.gabrielanceski.tccifrs.presentation.domain.request.RequirementCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.RequirementResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequirementService {
    private final RequirementRepository requirementRepository;
    private final ProjectService projectService;

    /**
     * Cria um novo requisito para um projeto.
     * Se o usuário autenticado não for o gerente do projeto, uma exceção será lançada.
     * O reporter será obrigatoriamente o gerente do projeto no momento que a requisição for enviada.
     * Se o usuário autenticado for ADMIN ou superior, e não for o gerente do projeto, da mesma forma, o reporter será o user vinculado ao projeto.
     * @param request Requisição.
     * @param authenticatedUser Usuário autenticado.
     * @return Resposta com os dados do requisito criado.
     */
    public RequirementResponse createRequirement(RequirementCreateRequest request, AuthenticatedUser authenticatedUser) {
        log.info("createRequirement() - creating new requirement - request <{}>", request);

        Project project = projectService.getProjectById(request.projectId());
        projectService.checkIfUserCanUpdateProject(project, authenticatedUser);

        RequirementLevel importanceLevel = RequirementLevel.fromString(request.importanceLevel());
        RequirementLevel effortLevel = RequirementLevel.fromString(request.effortLevel());

        Requirement requirement = new Requirement();
        requirement.setName(request.name());
        requirement.setDescription(request.description());
        requirement.setGoals(request.goals());
        requirement.setSpecs(request.specs());
        requirement.setCollaboration(request.collaboration());
        requirement.setInnovation(request.innovation());
        requirement.setRestrictions(request.restrictions());
        requirement.setImportanceLevel(importanceLevel);
        requirement.setEffortLevel(effortLevel);
        requirement.setProject(project);
        requirement.setReporter(project.getProjectManager());

        return RequirementResponse.fromEntity(requirementRepository.save(requirement));
    }

    public Requirement getRequirementById(String requirementId) {
        return requirementRepository.findById(requirementId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Requirement not found"));
    }

    public RequirementResponse getDetails(String requirementId) {
        return RequirementResponse.fromEntity(getRequirementById(requirementId));
    }
}
