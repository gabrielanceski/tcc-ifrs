package com.gabrielanceski.tccifrs.application.service;

import com.gabrielanceski.tccifrs.domain.Role;
import com.gabrielanceski.tccifrs.domain.entity.Company;
import com.gabrielanceski.tccifrs.domain.entity.Team;
import com.gabrielanceski.tccifrs.domain.entity.User;
import com.gabrielanceski.tccifrs.exception.UnknownRoleException;
import com.gabrielanceski.tccifrs.exception.UserNotFoundException;
import com.gabrielanceski.tccifrs.infrastructure.repository.CompanyRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.TeamRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.UserRepository;
import com.gabrielanceski.tccifrs.presentation.domain.request.UserCreateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.UserDetailsRequest;
import com.gabrielanceski.tccifrs.presentation.domain.request.UserUpdateRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.PasswordResetResponse;
import com.gabrielanceski.tccifrs.presentation.domain.response.UserResponse;
import com.gabrielanceski.tccifrs.utils.DocumentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Set;

@Service
@Slf4j
public record UserService(
    UserRepository userRepository,
    CompanyRepository companyRepository,
    TeamRepository teamRepository,
    PasswordEncoder passwordEncoder
) {
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Obtém todos os detalhes de um usuário, incluindo detalhes de equipes que ele possui qualquer tipo de associação
     * @param request UserDetailsRequest
     * @return Detalhes do usuário
     */
    public UserResponse getUserDetails(UserDetailsRequest request) {
        log.info("getUserDetails() - Getting user details: {}", request);

        User user = userRepository.findByDocument(request.document()).orElseThrow(UserNotFoundException::new);
        Set<Team> teams = teamRepository.findTeamsByUserId(user.getId());

        return UserResponse.fromEntity(user, teams);
    }

    public UserResponse createUser(UserCreateRequest request) {
        log.info("createUser() - creating user - request: <{}>", request);

        if (userRepository.findByDocument(request.document()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        if (Role.MASTER == Role.fromString(request.role())) throw new UnknownRoleException(request.role());

        if (DocumentUtils.getDocumentType(request.document()) == null) {
            throw new IllegalArgumentException("Invalid document number");
        }

        User user = new User();
        user.setName(request.name());
        user.setDocument(request.document());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setBlocked(false);
        user.setActive(true);
        user.setRole(Role.fromString(request.role()));

        userRepository.save(user);

        return UserResponse.fromEntity(user);
    }

    public UserResponse patchUser(String id, UserUpdateRequest request) {
        log.info("patchUser() - updating user <{}> - request: <{}>", id, request);

        if (request.role() == null || Role.MASTER == Role.fromString(request.role())) throw new UnknownRoleException(request.role());

        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        updateUser(user, request);
        userRepository.save(user);

        return UserResponse.fromEntity(user);
    }

    /**
     * Associa um usuário a uma empresa. Usuários que não pertencem a role COMPANY não poderão ser associados.
     * @param userId UUID do usuário
     * @param companyId UUID da empresa
     * @return User response
     */
    public UserResponse associateUserToCompany(String userId, String companyId) {
        log.info("associateUserToCompany() - Associating user <{}> to company <{}>", userId, companyId);

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (Role.COMPANY != user.getRole()) {
            throw new IllegalArgumentException("User must have a company role to be associated to a company");
        }

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new IllegalArgumentException("Company not found"));
        user.setCompany(company);
        userRepository.save(user);

        return UserResponse.fromEntity(user);
    }

    public PasswordResetResponse resetPassword(UserDetailsRequest request) {
        log.info("resetPassword() - Resetting password for user with document: <{}>", request.document());

        User user = userRepository.findByDocument(request.document()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        String newPassword = generateNewRandomPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("resetPassword() - Password reset for user <{}>", user.getDocument());

        return new PasswordResetResponse(user.getName(), user.getDocument(), newPassword);
    }

    private String generateNewRandomPassword() {
        int passwordLength = 10;
        String passwordChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder newPassword = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++) {
            newPassword.append(passwordChars.charAt(RANDOM.nextInt(passwordChars.length())));
        }

        return newPassword.toString();
    }

    private void updateUser(User user, UserUpdateRequest request) {
        if (request.name() != null) user.setName(request.name());
        if (request.role() != null) user.setRole(Role.fromString(request.role()));
        if (request.active() != null) user.setActive(request.active());
        if (request.blocked() != null) user.setBlocked(request.blocked());
    }
}
