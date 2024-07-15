package com.bankaccountmicroservice.bankaccount_microservice.services.impl;

import com.bankaccountmicroservice.bankaccount_microservice.dto.BankAccountDto;
import com.bankaccountmicroservice.bankaccount_microservice.dto.BankAccountGetDto;
import com.bankaccountmicroservice.bankaccount_microservice.dto.CustomerGetDto;
import com.bankaccountmicroservice.bankaccount_microservice.error.*;
import com.bankaccountmicroservice.bankaccount_microservice.models.BankAccount;
import com.bankaccountmicroservice.bankaccount_microservice.repositories.BankAccountRepository;
import com.bankaccountmicroservice.bankaccount_microservice.services.IBankAccountService;
import com.bankaccountmicroservice.bankaccount_microservice.util.EnumTypeBankAccount;
import com.bankaccountmicroservice.bankaccount_microservice.util.EnumTypeCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.Date;

@Service
public class BankAccountServiceImpl implements IBankAccountService {
    final int LIMITSAVINGACCCOUNT = 5;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private WebClient.Builder webClient;

    @Override
    public Mono<BankAccountGetDto> save(BankAccountDto bankAccountDto) {
        return webClient
                .build()
                .get()
                .uri("http://localhost:8090/api/customers/{id}", bankAccountDto.getCustomer())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CustomerNotFoundException("Customer not found")))
                .bodyToMono(CustomerGetDto.class)
                .flatMap(customerGetDto -> processSaveBankAccount(bankAccountDto, customerGetDto));
    }

    @Override
    public Mono<BankAccountGetDto> findById(String id) {
        return bankAccountRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new BankAccountNotFoundException("Bank Account not found")))
                .map(this::bankAccountToBankAccountGetDto);
    }

    @Override
    public Flux<BankAccountGetDto> findByCustomer(String customer) {
        return bankAccountRepository
                .findByCustomer(customer)
                .map(this::bankAccountToBankAccountGetDto);
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
        newBankAccount.setType(bankAccountDto.getType());
        newBankAccount.setCustomer(bankAccountDto.getCustomer());

        if(customer.getType() == EnumTypeCustomer.BUSINESS) {
            newBankAccount.setHolders(bankAccountDto.getHolders());
        }

        if(bankAccountDto.getType() == EnumTypeBankAccount.SAVING) {
            newBankAccount.setLimit(LIMITSAVINGACCCOUNT);
        } else if(bankAccountDto.getType() == EnumTypeBankAccount.CURRENTACCOUNT) {
            newBankAccount.setLimit(-1);
        } else {
            newBankAccount.setLimit(1);
        }

        return bankAccountRepository
                .save(newBankAccount)
                .map(this::bankAccountToBankAccountGetDto);
    }

    private BankAccountGetDto bankAccountToBankAccountGetDto(BankAccount bankAccount) {
        BankAccountGetDto bankAccountGetDto = new BankAccountGetDto();

        bankAccountGetDto.setId(bankAccount.getId());
        bankAccountGetDto.setNumber(bankAccount.getNumber());
        bankAccountGetDto.setDateCreated(bankAccount.getDateCreated());
        bankAccountGetDto.setBalance(bankAccount.getBalance());
        bankAccountGetDto.setLimit(bankAccount.getLimit());
        bankAccountGetDto.setType(bankAccount.getType());
        bankAccountGetDto.setCustomer(bankAccount.getCustomer());
        bankAccountGetDto.setHolders(bankAccount.getHolders());

        return bankAccountGetDto;
    }
}