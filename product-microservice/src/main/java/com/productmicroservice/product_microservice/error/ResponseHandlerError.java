package com.productmicroservice.product_microservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResponseHandlerError {

    @ExceptionHandler(WebExchangeBindException.class)
    protected Mono<ResponseEntity<ErrorResponse>> handleMethodArgumentNotValid(WebExchangeBindException ex) {
        ErrorResponse errorResponse = new ErrorResponse();

        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .filter(message -> message != null && !message.contains("no debe estar vacío"))
                .collect(Collectors.toList());

        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(null);
        errorResponse.setErrors(errors);

        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BankAccountAlreadyExistsException.class)
    protected Mono<ResponseEntity<ErrorResponse>> handleBankAccountAlreadyExistsException(BankAccountAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setErrors(null);

        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(InvalidBankAccountTypeException.class)
    protected Mono<ResponseEntity<ErrorResponse>> handleBankAccountAlreadyExistsException(InvalidBankAccountTypeException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setErrors(null);

        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST));
    }
}