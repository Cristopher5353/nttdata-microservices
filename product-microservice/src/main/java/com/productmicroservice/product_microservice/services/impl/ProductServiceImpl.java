package com.productmicroservice.product_microservice.services.impl;

import com.productmicroservice.product_microservice.dto.BankAccountDto;
import com.productmicroservice.product_microservice.dto.BankAccountGetDto;
import com.productmicroservice.product_microservice.dto.CustomerGetDto;
import com.productmicroservice.product_microservice.error.BankAccountAlreadyExistsException;
import com.productmicroservice.product_microservice.error.CustomerNotFoundException;
import com.productmicroservice.product_microservice.error.InvalidBankAccountTypeException;
import com.productmicroservice.product_microservice.error.InvalidCustomerTypeException;
import com.productmicroservice.product_microservice.models.BankAccount;
import com.productmicroservice.product_microservice.repositories.BankAccountRepository;
import com.productmicroservice.product_microservice.services.IProductService;
import com.productmicroservice.product_microservice.util.Customer;
import com.productmicroservice.product_microservice.util.EnumTypeBankAccount;
import com.productmicroservice.product_microservice.util.EnumTypeCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.Date;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private WebClient.Builder webClient;

    @Override
    public Mono<BankAccountGetDto> saveBankAccount(BankAccountDto bankAccountDto) {
        return webClient
                .build()
                .get()
                .uri("http://localhost:8001/api/customers/{id}", bankAccountDto.getCustomer())
                .retrieve()
                .bodyToMono(CustomerGetDto.class)
                .flatMap(customerGetDto -> {
                    if(customerGetDto.getId() == null) {
                        return Mono.error(new CustomerNotFoundException("Customer not found"));
                    }
                    return processSaveBankAccount(bankAccountDto, customerGetDto);
                });
    }

    private Mono<BankAccountGetDto> processSaveBankAccount(BankAccountDto bankAccountDto, CustomerGetDto customerGetDto) {
        if (customerGetDto.getType() == EnumTypeCustomer.PERSONAL) {
            return processPersonalCustomer(bankAccountDto, customerGetDto);
        } else if (customerGetDto.getType() == EnumTypeCustomer.BUSINESS) {
            return processBusinessCustomer(bankAccountDto, customerGetDto);
        } else {
            return Mono.error(new InvalidCustomerTypeException("Invalid customer type"));
        }
    }

    private Mono<BankAccountGetDto> processPersonalCustomer(BankAccountDto bankAccountDto, CustomerGetDto customerGetDto) {
        if (bankAccountDto.getType() == EnumTypeBankAccount.SAVING || bankAccountDto.getType() == EnumTypeBankAccount.CURRENTACCOUNT) {
            return bankAccountRepository.findByCustomerAndTypeIn(bankAccountDto.getCustomer(), Arrays.asList(EnumTypeBankAccount.SAVING, EnumTypeBankAccount.CURRENTACCOUNT))
                    .collectList()
                    .flatMap(existingAccounts -> {
                        if (!existingAccounts.isEmpty()) {
                            return Mono.error(new BankAccountAlreadyExistsException("Personal customer already has a saving or current account"));
                        } else {
                            return createNewBankAccount(bankAccountDto, customerGetDto);
                        }
                    });
        } else {
            return createNewBankAccount(bankAccountDto, customerGetDto);
        }
    }

    private Mono<BankAccountGetDto> processBusinessCustomer(BankAccountDto bankAccountDto, CustomerGetDto customerGetDto) {
        if (bankAccountDto.getType() == EnumTypeBankAccount.SAVING || bankAccountDto.getType() == EnumTypeBankAccount.FIXEDTERM) {
            return Mono.error(new InvalidBankAccountTypeException("Business customers cannot have savings or fixed term accounts"));
        } else {
            return createNewBankAccount(bankAccountDto, customerGetDto);
        }
    }

    private Mono<BankAccountGetDto> createNewBankAccount(BankAccountDto bankAccountDto, CustomerGetDto customer) {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setNumber(bankAccountDto.getNumber());
        newBankAccount.setDateCreated(new Date());
        newBankAccount.setBalance(bankAccountDto.getBalance());
        newBankAccount.setCommission(bankAccountDto.getCommission());
        newBankAccount.setLimit(bankAccountDto.getLimit());
        newBankAccount.setType(bankAccountDto.getType());
        newBankAccount.setCustomer(bankAccountDto.getCustomer());

        if(customer.getType() == EnumTypeCustomer.BUSINESS) {
            newBankAccount.setHolders(bankAccountDto.getHolders());
        }

        return bankAccountRepository
                .save(newBankAccount)
                .map(this::bankAccountToBankAccount);
    }

    private BankAccountGetDto bankAccountToBankAccount(BankAccount bankAccount) {
        BankAccountGetDto bankAccountGetDto = new BankAccountGetDto();

        bankAccountGetDto.setId(bankAccount.getId());
        bankAccountGetDto.setNumber(bankAccount.getNumber());
        bankAccountGetDto.setDateCreated(bankAccount.getDateCreated());
        bankAccountGetDto.setBalance(bankAccount.getBalance());
        bankAccountGetDto.setCommission(bankAccount.getCommission());
        bankAccountGetDto.setLimit(bankAccount.getLimit());
        bankAccountGetDto.setType(bankAccount.getType());
        bankAccountGetDto.setCustomer(bankAccount.getCustomer());
        bankAccountGetDto.setHolders(bankAccount.getHolders());

        return bankAccountGetDto;
    }
}