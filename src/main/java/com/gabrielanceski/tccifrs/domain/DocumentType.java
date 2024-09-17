package com.gabrielanceski.tccifrs.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DocumentType {
    CPF("CPF", 11),
    CNPJ("CNPJ", 14),
    SIAPE("SIAPE", 7),
    STUDENT_REGISTRATION_NUMBER("SRN", 10);

    private final String name;
    private final int length;

    DocumentType(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public static DocumentType getByLength(int length) {
        return Arrays.stream(values())
                .filter(documentType -> documentType.getLength() == length)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid document length <" + length + ">"));
    }
}
