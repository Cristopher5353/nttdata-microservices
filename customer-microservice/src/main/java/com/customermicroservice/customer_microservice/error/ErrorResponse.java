package com.customermicroservice.customer_microservice.error;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String message;
    private List<String> errors;
}
