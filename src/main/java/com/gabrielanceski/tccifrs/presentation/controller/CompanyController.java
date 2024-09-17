package com.gabrielanceski.tccifrs.presentation.controller;

import com.gabrielanceski.tccifrs.application.service.CompanyService;
import com.gabrielanceski.tccifrs.presentation.domain.request.CompanyRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.CompanyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public record CompanyController(CompanyService companyService) {

    // TODO: empresas podem ser cadastradas apenas por um usuário tipo ADMIN ou superior.
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.createCompany(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CompanyResponse> patchCompany(@PathVariable String id, @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.patchCompany(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable String id) {
        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> listCompanies() {
        return ResponseEntity.ok(companyService.listCompanies());
    }
}
