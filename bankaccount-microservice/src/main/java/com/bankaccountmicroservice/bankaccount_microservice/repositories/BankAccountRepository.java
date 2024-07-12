package com.bankaccountmicroservice.bankaccount_microservice.repositories;

import com.bankaccountmicroservice.bankaccount_microservice.models.BankAccount;
import com.bankaccountmicroservice.bankaccount_microservice.util.EnumTypeBankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import java.util.List;

public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {
    Flux<BankAccount> findByCustomerAndTypeIn(String customer, List<EnumTypeBankAccount> types);
    Flux<BankAccount> findByCustomer(String customer);
}