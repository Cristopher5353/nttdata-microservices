package com.customermicroservice.customer_microservice.controllers;

import com.customermicroservice.customer_microservice.dto.CustomerGetDto;
import com.customermicroservice.customer_microservice.dto.CustomerDto;
import com.customermicroservice.customer_microservice.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private ICustomerService iCustomerService;

    @PostMapping("")
    public Mono<CustomerGetDto> save(@Valid @RequestBody CustomerDto customerSaveDto) {
        return iCustomerService.save(customerSaveDto);
    }

    @GetMapping("")
    public Flux<CustomerGetDto> findAll() {
        return iCustomerService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<CustomerGetDto> findById(@PathVariable String id) {
        return iCustomerService.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<CustomerGetDto> update(@PathVariable String id, @Valid @RequestBody CustomerDto customerDto) {
        return iCustomerService.update(id, customerDto);
    }

    @DeleteMapping("/{id}")
    public Mono<String> delete(@PathVariable String id) {
        return iCustomerService.delete(id);
    }
}