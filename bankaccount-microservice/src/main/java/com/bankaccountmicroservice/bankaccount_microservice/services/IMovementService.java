package com.bankaccountmicroservice.bankaccount_microservice.services;

import com.bankaccountmicroservice.bankaccount_microservice.dto.MovementDto;
import com.bankaccountmicroservice.bankaccount_microservice.dto.MovementGetDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IMovementService {
    Mono<MovementGetDto> movement(MovementDto movementDto);
    Flux<MovementGetDto> findByBankAccountAndCustomer(String bankAccount, String customer);
}