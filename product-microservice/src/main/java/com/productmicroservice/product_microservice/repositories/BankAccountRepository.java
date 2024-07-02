package com.productmicroservice.product_microservice.repositories;

import com.productmicroservice.product_microservice.models.BankAccount;
import com.productmicroservice.product_microservice.util.EnumTypeBankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {
    Flux<BankAccount> findByCustomerAndTypeIn(String customer, List<EnumTypeBankAccount> types);
}