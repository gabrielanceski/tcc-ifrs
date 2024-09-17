package com.gabrielanceski.tccifrs.infrastructure.repository;

import com.gabrielanceski.tccifrs.domain.entity.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends CrudRepository<Company, String> {
    Optional<Company> findByDocument(String document);
    @NonNull List<Company> findAll();
}
