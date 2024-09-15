package com.gabrielanceski.tccifrs.presentation.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FaultResponse {
    private String message;
    private int statusCode;
}
