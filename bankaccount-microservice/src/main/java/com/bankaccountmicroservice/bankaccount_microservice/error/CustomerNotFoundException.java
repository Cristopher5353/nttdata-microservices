package com.bankaccountmicroservice.bankaccount_microservice.error;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(String message) {
        super(message);
    }
}