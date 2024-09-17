package com.gabrielanceski.tccifrs.presentation.domain.response;

import com.gabrielanceski.tccifrs.domain.entity.Company;

public record CompanyResponse(
    String id,
    String name,
    String document,
    String address,
    String contacts
) {
    public static CompanyResponse fromEntity(Company company) {
        return new CompanyResponse(
            company.getId(),
            company.getName(),
            company.getDocument(),
            company.getAddress(),
            company.getContacts()
        );
    }
}
