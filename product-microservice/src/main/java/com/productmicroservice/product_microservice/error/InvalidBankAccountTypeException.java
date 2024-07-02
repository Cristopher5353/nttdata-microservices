package com.productmicroservice.product_microservice.error;

public class InvalidBankAccountTypeException extends RuntimeException{
    public InvalidBankAccountTypeException(String message) {
        super(message);
    }
}