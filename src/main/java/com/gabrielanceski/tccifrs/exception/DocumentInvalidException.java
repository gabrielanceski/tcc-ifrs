package com.gabrielanceski.tccifrs.exception;

import com.gabrielanceski.tccifrs.domain.DocumentType;

public class DocumentInvalidException extends InvalidDataException {

    public DocumentInvalidException(DocumentType documentType) {
        super("Invalid " + documentType.name());
    }
}
