package com.customermicroservice.customer_microservice.services;

import com.customermicroservice.customer_microservice.dto.CustomerGetDto;
import com.customermicroservice.customer_microservice.dto.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService {
    Mono<CustomerGetDto> save(CustomerDto customerDto);
    Flux<CustomerGetDto> findAll();
    Mono<CustomerGetDto> findById(String id);
    Mono<CustomerGetDto> update(String id, CustomerDto customerDto);
    Mono<String> delete(String id);
}