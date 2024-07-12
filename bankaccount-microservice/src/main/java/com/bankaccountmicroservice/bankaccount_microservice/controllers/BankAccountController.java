package com.bankaccountmicroservice.bankaccount_microservice.controllers;

import com.bankaccountmicroservice.bankaccount_microservice.dto.BankAccountDto;
import com.bankaccountmicroservice.bankaccount_microservice.dto.BankAccountGetDto;
import com.bankaccountmicroservice.bankaccount_microservice.services.IBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.ws.rs.GET;

@RestController
@RequestMapping("/api/bank-accounts")
public class BankAccountController {
    @Autowired
    private IBankAccountService iBankAccountService;

    @PostMapping("")
    public Mono<BankAccountGetDto> save(@RequestBody BankAccountDto bankAccountDto) {
        return iBankAccountService.save(bankAccountDto);
    }

    @GetMapping("/{id}")
    public Mono<BankAccountGetDto> findById(@PathVariable String id) {
        return iBankAccountService.findById(id);
    }

    @GetMapping("/customers/{customer}")
    public Flux<BankAccountGetDto> findByCustomer(@PathVariable String customer) {
        return iBankAccountService.findByCustomer(customer);
    }
}