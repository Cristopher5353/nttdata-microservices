package com.bankaccountmicroservice.bankaccount_microservice.error;

public class BankAccountAlreadyExistsException extends RuntimeException {
    public BankAccountAlreadyExistsException(String message) {
        super(message);
    }
}