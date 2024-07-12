package com.bankaccountmicroservice.bankaccount_microservice.services;

import com.bankaccountmicroservice.bankaccount_microservice.dto.BankAccountDto;
import com.bankaccountmicroservice.bankaccount_microservice.dto.BankAccountGetDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountService {
    Mono<BankAccountGetDto> save(BankAccountDto bankAccountDto);
    Mono<BankAccountGetDto> findById(String id);
    Flux<BankAccountGetDto> findByCustomer(String customer);
}