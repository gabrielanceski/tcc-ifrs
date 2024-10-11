package com.gabrielanceski.tccifrs.presentation.controller;

import com.gabrielanceski.tccifrs.application.service.CompanyService;
import com.gabrielanceski.tccifrs.domain.CurrentUser;
import com.gabrielanceski.tccifrs.domain.impl.AuthenticatedUser;
import com.gabrielanceski.tccifrs.presentation.domain.request.CompanyRequest;
import com.gabrielanceski.tccifrs.presentation.domain.response.CompanyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public record CompanyController(CompanyService companyService) {

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CompanyRequest request, @CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(companyService.createCompany(request, authenticatedUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CompanyResponse> patchCompany(@PathVariable String id, @RequestBody CompanyRequest request, @CurrentUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(companyService.patchCompany(id, request, authenticatedUser));
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
