package com.bankaccountmicroservice.bankaccount_microservice.error;

public class InvalidCustomerTypeException extends RuntimeException{
    public InvalidCustomerTypeException(String message) {
        super(message);
    }
}