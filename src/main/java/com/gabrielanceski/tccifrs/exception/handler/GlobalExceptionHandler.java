package com.gabrielanceski.tccifrs.exception.handler;

import com.gabrielanceski.tccifrs.exception.InvalidDataException;
import com.gabrielanceski.tccifrs.presentation.domain.response.FaultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleGenericExceptions(Exception exception, WebRequest request) {
        log.error("handleGenericExceptions() - message <{}> - error: ", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            new FaultResponse(
                "Internal server error.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
            )
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public final ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException exception, WebRequest request) {
        log.error("handleValidationExceptions() - message <{}> - error: ", exception.getMessage(), exception);

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors()
            .stream()
            .filter((error) -> error instanceof FieldError)
            .map((error) -> (FieldError) error)
            .forEach((fieldError) -> {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            }
        );

        if (errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new FaultResponse(
                    "Invalid input data.",
                    HttpStatus.BAD_REQUEST.value()
                )
            );
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new FaultResponse(
                errors.toString(),
                HttpStatus.BAD_REQUEST.value()
            )
        );
    }

    @ExceptionHandler({DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class, HttpMessageNotReadableException.class})
    public final ResponseEntity<Object> handleUnprocessableExceptions(Exception exception, WebRequest request) {
        log.error("handleUnprocessableExceptions() - message <{}> - error: ", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                new FaultResponse(
                    "Something went wrong between the given data and the processing.",
                    HttpStatus.UNPROCESSABLE_ENTITY.value()
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
