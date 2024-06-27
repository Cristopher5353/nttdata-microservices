package com.customermicroservice.customer_microservice.repositories;

import com.customermicroservice.customer_microservice.models.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}