package com.bankaccountmicroservice.bankaccount_microservice.error;

public class BankAccountNotFoundException extends RuntimeException{
    public BankAccountNotFoundException(String message) {
        super(message);
    }
}