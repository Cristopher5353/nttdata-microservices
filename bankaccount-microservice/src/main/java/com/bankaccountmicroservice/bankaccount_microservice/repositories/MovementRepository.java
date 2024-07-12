package com.bankaccountmicroservice.bankaccount_microservice.repositories;

import com.bankaccountmicroservice.bankaccount_microservice.models.Movement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import java.util.Date;

public interface MovementRepository extends ReactiveMongoRepository<Movement, String> {
    Flux<Movement> findByCustomerAndBankAccountAndDateCreatedBetween(String customer, String bankAccount, Date startDate, Date endDate);
    Flux<Movement> findByBankAccountAndCustomer(String bankAccount, String customer);
}