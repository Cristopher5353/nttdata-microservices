package com.bankaccountmicroservice.bankaccount_microservice.error;

public class MovementLimitException extends RuntimeException {
    public MovementLimitException(String message) {
        super(message);
    }
}