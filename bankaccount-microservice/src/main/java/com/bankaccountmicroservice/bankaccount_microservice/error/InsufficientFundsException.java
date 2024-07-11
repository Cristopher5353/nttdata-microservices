package com.bankaccountmicroservice.bankaccount_microservice.error;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}