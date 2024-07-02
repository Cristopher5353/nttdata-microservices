package com.productmicroservice.product_microservice.controllers;

import com.productmicroservice.product_microservice.dto.BankAccountDto;
import com.productmicroservice.product_microservice.dto.BankAccountGetDto;
import com.productmicroservice.product_microservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @PostMapping("/bank-accounts")
    public Mono<BankAccountGetDto> saveBankAccount(@RequestBody BankAccountDto bankAccountDto) {
        return iProductService.saveBankAccount(bankAccountDto);
    }

    @PostMapping("/credits")
    public String saveCredit() {
        //Agregar cuenta crédito
        return null;
    }
}
