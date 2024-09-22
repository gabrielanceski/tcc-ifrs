package com.gabrielanceski.tccifrs.exception.handler;

import com.gabrielanceski.tccifrs.exception.InvalidDataException;
import com.gabrielanceski.tccifrs.presentation.domain.response.FaultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleGenericExceptions(Exception exception, WebRequest request) {
        log.error("handleGenericExceptions() - message <{}> - error: ", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            new FaultResponse(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            )
        );
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public final ResponseEntity<Object> handleSQLExceptions(Exception exception, WebRequest request) {
        log.error("handleSQLExceptions() - message <{}> - error: ", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new FaultResponse(
                        "Internal server error",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                )
        );
    }

    @ExceptionHandler({DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class})
    public final ResponseEntity<Object> handleDataIntegrityViolation(Exception exception, WebRequest request) {
        log.error("handleDataIntegrityViolation() - message <{}> - error: ", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new FaultResponse(
                        "Duplicate entry",
                        HttpStatus.CONFLICT.value()
                )
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<Object> handleResponseStatusExceptions(ResponseStatusException exception, WebRequest request) {
        log.error("handleResponseStatusExceptions() - message <{}> - error: ", exception.getMessage(), exception);
        return ResponseEntity.status(exception.getStatusCode()).body(
            new FaultResponse(
                exception.getReason(),
                exception.getStatusCode().value()
            )
        );
    }

    @ExceptionHandler({InvalidDataException.class, IllegalArgumentException.class})
    public final ResponseEntity<Object> handleDataExceptions(Exception exception, WebRequest request) {
        log.error("handleDataExceptions() - message <{}> - error: ", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new FaultResponse(
                        exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value()
                )
        );
    }

}
