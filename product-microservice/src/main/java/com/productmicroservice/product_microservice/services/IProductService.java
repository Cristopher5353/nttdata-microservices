package com.productmicroservice.product_microservice.services;

import com.productmicroservice.product_microservice.dto.BankAccountDto;
import com.productmicroservice.product_microservice.dto.BankAccountGetDto;
import reactor.core.publisher.Mono;

public interface IProductService {
    Mono<BankAccountGetDto> saveBankAccount(BankAccountDto bankAccountDto);
}