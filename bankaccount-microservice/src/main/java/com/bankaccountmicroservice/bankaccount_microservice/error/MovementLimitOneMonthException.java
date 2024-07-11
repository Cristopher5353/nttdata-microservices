package com.bankaccountmicroservice.bankaccount_microservice.error;

public class MovementLimitOneMonthException extends RuntimeException{
    public MovementLimitOneMonthException(String message) {
        super(message);
    }
}