package com.gabrielanceski.tccifrs.application.service;

import com.gabrielanceski.tccifrs.domain.Role;
import com.gabrielanceski.tccifrs.domain.TaskStatus;
import com.gabrielanceski.tccifrs.domain.entity.Project;
import com.gabrielanceski.tccifrs.domain.entity.Requirement;
import com.gabrielanceski.tccifrs.domain.entity.Task;
import com.gabrielanceski.tccifrs.domain.entity.Team;
import com.gabrielanceski.tccifrs.domain.impl.AuthenticatedUser;
import com.gabrielanceski.tccifrs.infrastructure.repository.TaskRepository;
import com.gabrielanceski.tccifrs.presentation.domain.request.TaskCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.TaskUpdateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.TaskResponse;
import com.gabrielanceski.tccifrs.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final RequirementService requirementService;
    private final ProjectService projectService;
    private final TeamService teamService;

    public TaskResponse createTask(TaskCreateRequest request, AuthenticatedUser authenticatedUser) {
        log.info("createTask() - creating new task - request <{}>", request);

        Requirement requirement = requirementService.getRequirementById(request.requirementId());
        Project project = projectService.getProjectById(requirement.getProject().getId());

        checkIfUserCanUpdateOrCreateTask(authenticatedUser, project);

        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setRequirement(requirement);
        task.setStatus(TaskStatus.CREATED);

        return TaskResponse.fromEntity(taskRepository.save(task));
    }

    public TaskResponse updateTask(String id, TaskUpdateRequest request, AuthenticatedUser authenticatedUser) {
        log.info("updateTask() - updating task - request <{}>", request);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        checkIfTaskCanBeUpdated(task);
        checkIfUserCanUpdateOrCreateTask(authenticatedUser, task.getRequirement().getProject());

        if (ObjectUtils.nonNullOrEmpty(request.getTitle())) task.setTitle(request.getTitle());
        if (ObjectUtils.nonNullOrEmpty(request.getDescription())) task.setDescription(request.getDescription());
        if (ObjectUtils.nonNullOrEmpty(request.getStatus())) task.setStatus(TaskStatus.fromString(request.getStatus()));
        if (ObjectUtils.nonNullOrEmpty(request.getRequirementId())) {
            Requirement requirement = requirementService.getRequirementById(request.getRequirementId());
            task.setRequirement(requirement);
        }
        if (ObjectUtils.nonNullOrEmpty(request.getTeamId())) {
            Team team = teamService.getTeamById(request.getTeamId());
            task.setTeam(team);
        }
        if (ObjectUtils.nonNullOrEmpty(request.getBlockedById())) {
            Task blockedBy = taskRepository.findById(request.getBlockedById())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
            task.setBlockedBy(blockedBy);
        }

        return TaskResponse.fromEntity(taskRepository.save(task));
    }

    public TaskResponse getDetails(String id) {
        return TaskResponse.fromEntity(taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")));
    }

    public List<TaskResponse> listAllByRequirementId(String requirementId) {
        return taskRepository.findAllByRequirementId(requirementId).stream()
                .map(TaskResponse::fromEntity)
                .toList();
    }

    public List<TaskResponse> listAllByProjectId(String projectId) {
        return taskRepository.findAllByProjectId(projectId).stream()
                .map(TaskResponse::fromEntity)
                .toList();
    }

    /**
     * Cancela uma tarefa.
     * Apenas projectManagers ou ADMIN+ podem cancelar tarefas.
     * @param taskId Identificador da tarefa.
     * @param authenticatedUser Usuário autenticado.
     */
    public void cancelTask(String taskId, AuthenticatedUser authenticatedUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        checkIfUserCanDeleteTask(authenticatedUser, task);

        task.setStatus(TaskStatus.CANCELED);
        taskRepository.save(task);
    }

    private void checkIfTaskCanBeUpdated(Task task) {
        if (TaskStatus.CANCELED == task.getStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task is canceled and cannot be updated.");
        }

        if (TaskStatus.DONE == task.getStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task is done and cannot be updated.");
        }
    }

    private void checkIfUserCanDeleteTask(AuthenticatedUser authenticatedUser, Task task) {
        if (Role.ADMIN == authenticatedUser.getEntity().getRole() || Role.MASTER == authenticatedUser.getEntity().getRole()) {
            return;
        }

        if (task.getRequirement().getProject().getProjectManager().getId().equals(authenticatedUser.getEntity().getId())) {
            return;
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not allowed to delete tasks.");
    }

    private void checkIfUserCanUpdateOrCreateTask(AuthenticatedUser authenticatedUser, Project project) {
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        Role userRole = authenticatedUser.getEntity().getRole();

        if (Role.MASTER == userRole || Role.ADMIN == userRole) {
            return;
        }

        List<String> teamLeaders = projectService.getAllProjectTeamLeaders(project.getId());
        if (project.getProjectManager().getId().equals(authenticatedUser.getEntity().getId()) ||
                teamLeaders.contains(authenticatedUser.getEntity().getId())) {
            return;
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not allowed to create or update tasks for this project.");
    }
}
