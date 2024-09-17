package com.gabrielanceski.tccifrs.application.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielanceski.tccifrs.domain.DocumentType;
import com.gabrielanceski.tccifrs.domain.entity.Company;
import com.gabrielanceski.tccifrs.exception.AddressNotProvidedException;
import com.gabrielanceski.tccifrs.exception.DocumentInvalidException;
import com.gabrielanceski.tccifrs.infrastructure.repository.CompanyRepository;
import com.gabrielanceski.tccifrs.presentation.domain.request.CompanyRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.CompanyResponse;
import com.gabrielanceski.tccifrs.utils.DocumentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public record CompanyService(CompanyRepository companyRepository, ObjectMapper objectMapper) {

    public CompanyResponse createCompany(CompanyRequest request) {
        log.info("createCompany() - creating company - request: <{}>", request);

        if (!DocumentUtils.checkCNPJ(request.document())) {
            throw new DocumentInvalidException(DocumentType.CNPJ);
        }

        if (request.address() == null || request.address().isBlank()) {
            throw new AddressNotProvidedException();
        }

        if (companyRepository.findByDocument(request.document()).isPresent()) {
            throw new IllegalArgumentException("Company already exists");
        }

        Company company = new Company();
        company.setDocument(request.document());
        company.setName(request.name());
        company.setAddress(request.address());
        company.setContacts(request.contacts());

        companyRepository.save(company);

        return CompanyResponse.fromEntity(company);
    }

    public CompanyResponse patchCompany(String id, CompanyRequest request) {
        log.info("patchCompany() - patching company - request: <{}>", request);

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Company id not provided");
        }

        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        if (company.getDocument().equalsIgnoreCase(request.document())) {
            throw new IllegalArgumentException("Document cannot be changed");
        }

        try {
            objectMapper.updateValue(company, request);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }

        companyRepository.save(company);

        return CompanyResponse.fromEntity(company);
    }

    public CompanyResponse getCompany(String id) {
        log.info("getCompany() - getting company - id: <{}>", id);

        Company company = companyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        return CompanyResponse.fromEntity(company);
    }

    public List<CompanyResponse> listCompanies() {
        log.info("listCompanies() - listing companies");

        List<Company> companies = companyRepository.findAll();

        return companies.stream()
            .map(CompanyResponse::fromEntity).toList();
    }
}
