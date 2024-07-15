package com.customermicroservice.customer_microservice.services.impl;

import com.customermicroservice.customer_microservice.dto.CustomerGetDto;
import com.customermicroservice.customer_microservice.dto.CustomerDto;
import com.customermicroservice.customer_microservice.error.CustomerNotFoundException;
import com.customermicroservice.customer_microservice.models.Customer;
import com.customermicroservice.customer_microservice.repositories.CustomerRepository;
import com.customermicroservice.customer_microservice.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerImpl implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Mono<CustomerGetDto> save(CustomerDto customerDto) {
        return customerRepository.save(customerDtoToCustomer(customerDto))
                .map(this::customerToCustomerGetDto);
    }

    @Override
    public Flux<CustomerGetDto> findAll() {
        return customerRepository
                .findAll()
                .map(this::customerToCustomerGetDto);
    }

    @Override
    public Mono<CustomerGetDto> findById(String id) {
        return customerRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found")))
                .map(this::customerToCustomerGetDto);
    }

    @Override
    public Mono<CustomerGetDto> update(String id, CustomerDto customerDto) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found")))
                .flatMap(customer -> updateCustomerFields(customer, customerDto));
    }

    @Override
    public Mono<String> delete(String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found")))
                .flatMap(customer -> customerRepository.deleteById(id)
                        .then(Mono.just("Customer deleted successfully")));
    }

    private Customer customerDtoToCustomer(CustomerDto customerSaveDto) {
        Customer customer = new Customer();
        customer.setName(customerSaveDto.getName());
        customer.setLastName(customerSaveDto.getLastName());
        customer.setAge(customerSaveDto.getAge());
        customer.setType(customerSaveDto.getType());

        return customer;
    }

    private CustomerGetDto customerToCustomerGetDto(Customer customer) {
        CustomerGetDto customerGetDto = new CustomerGetDto();

        customerGetDto.setId(customer.getId());
        customerGetDto.setName(customer.getName());
        customerGetDto.setLastName(customer.getLastName());
        customerGetDto.setAge(customer.getAge());
        customerGetDto.setType(customer.getType());

        return customerGetDto;
    }

    private Mono<CustomerGetDto> updateCustomerFields(Customer customer, CustomerDto customerDto) {
        customer.setName(customerDto.getName());
        customer.setLastName(customerDto.getLastName());
        customer.setAge(customerDto.getAge());
        customer.setType(customerDto.getType());

        return customerRepository.save(customer)
                .map(this::customerToCustomerGetDto);
    }
}