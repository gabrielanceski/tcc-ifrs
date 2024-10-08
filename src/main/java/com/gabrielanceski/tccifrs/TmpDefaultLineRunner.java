package com.gabrielanceski.tccifrs;

import com.gabrielanceski.tccifrs.domain.ProjectStatus;
import com.gabrielanceski.tccifrs.domain.Role;
import com.gabrielanceski.tccifrs.domain.entity.Company;
import com.gabrielanceski.tccifrs.domain.entity.Project;
import com.gabrielanceski.tccifrs.domain.entity.User;
import com.gabrielanceski.tccifrs.infrastructure.repository.CompanyRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.ProjectRepository;
import com.gabrielanceski.tccifrs.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TmpDefaultLineRunner {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    @Bean
    CommandLineRunner init() {
        return args -> {
            createUser("Desenvolvedor", "88888888888", "135790", Role.MASTER);
            createUser("Aluno Teste", "2020123456", "135790", Role.STUDENT);
            createUser("Professor Teste", "1234567", "135790", Role.PROFESSOR);

            createCompanyAndUsers();

            createProject(userRepository.findByDocument("1234567").orElseThrow(), companyRepository.findByDocument("10637926000146").orElseThrow());
        };
    }

    /**
     * Por propósitos de teste, o ID pode ser arbitrário.
     * @param name
     * @param document
     * @param password
     * @param role
     */
    private User createUser(String name, String document, String password, Role role) {
        User user = new User();
        user.setName(name);
        user.setDocument(document);
        user.setPassword(passwordEncoder.encode(password));
        user.setBlocked(false);
        user.setActive(true);
        user.setRole(role);

        log.info("Saving user: {}", user);
        return userRepository.save(user);
    }

    private Company createCompany() {
        Company company = new Company();
        company.setName("IFRS - Campus Bento Gonçalves");
        company.setAddress("Av. Osvaldo Aranha, 540 - Juventude da Enologia, Bento Gonçalves - RS, 95700-000");
        company.setDocument("10637926000146");
        company.setContacts("5434553200");

        return companyRepository.save(company);
    }

    private void createCompanyAndUsers() {
        User companyUser = createUser("Usuario empresa", "11122233344", "135790", Role.COMPANY);
        Company company = createCompany();
        companyUser.setCompany(company);
        userRepository.save(companyUser);
    }

    private void createProject(User po, Company company) {
        Project project = new Project();
        project.setName("Projeto Teste");
        project.setStatus(ProjectStatus.CREATED);
        project.setDescription("Projeto de teste para fins de desenvolvimento");
        project.setProjectManager(po);
        project.setCompany(company);
        project.setTeams(Set.of());
        project.setRequirements(Set.of());

        log.info("Saving project: {}", project);

        projectRepository.save(project);
    }

}
