package com.productmicroservice.product_microservice.error;

public class InvalidCustomerTypeException extends RuntimeException{
    public InvalidCustomerTypeException(String message) {
        super(message);
    }
}