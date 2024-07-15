package com.bankaccountmicroservice.bankaccount_microservice.services.impl;

import com.bankaccountmicroservice.bankaccount_microservice.dto.CustomerGetDto;
import com.bankaccountmicroservice.bankaccount_microservice.dto.MovementDto;
import com.bankaccountmicroservice.bankaccount_microservice.dto.MovementGetDto;
import com.bankaccountmicroservice.bankaccount_microservice.error.*;
import com.bankaccountmicroservice.bankaccount_microservice.models.BankAccount;
import com.bankaccountmicroservice.bankaccount_microservice.models.Movement;
import com.bankaccountmicroservice.bankaccount_microservice.repositories.BankAccountRepository;
import com.bankaccountmicroservice.bankaccount_microservice.repositories.MovementRepository;
import com.bankaccountmicroservice.bankaccount_microservice.services.IMovementService;
import com.bankaccountmicroservice.bankaccount_microservice.util.EnumTypeBankAccount;
import com.bankaccountmicroservice.bankaccount_microservice.util.EnumTypeMovement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class MovementServiceImpl implements IMovementService {
    @Autowired
    private WebClient.Builder webClient;

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public Mono<MovementGetDto> movement(MovementDto movementDto) {
        return getCustomerGetDto(movementDto.getCustomer())
                .flatMap(customer -> bankAccountRepository.findById(movementDto.getBankAccount())
                        .switchIfEmpty(Mono.error(new BankAccountNotFoundException("Bank account not found")))
                        .flatMap(bankAccount -> checkMovementLimits(movementDto, bankAccount)
                                .flatMap(valid -> processMovement(movementDto, bankAccount))
                                .map(this::movementToMovementGetDto)
                        )
                );
    }

    @Override
    public Flux<MovementGetDto> findByBankAccountAndCustomer(String bankAccount, String customer) {
        return movementRepository
                .findByBankAccountAndCustomer(bankAccount, customer)
                .map(this::movementToMovementGetDto);
    }

    private Mono<Boolean> checkMovementLimits(MovementDto movementDto, BankAccount bankAccount) {
        if(bankAccount.getType() != EnumTypeBankAccount.SAVING && bankAccount.getType() != EnumTypeBankAccount.FIXEDTERM) {
            return Mono.just(true);
        }

        return movementRepository
                .findByCustomerAndBankAccountAndDateCreatedBetween(movementDto.getCustomer(), movementDto.getBankAccount(), getStartOfMonth(), getEndOfMonth())
                .collectList()
                .flatMap(movements -> {
                    if(bankAccount.getType() == EnumTypeBankAccount.SAVING && movements.size() >= bankAccount.getLimit()) {
                        return Mono.error(new MovementLimitException("The client has reached the monthly transaction limit"));
                    } else if(bankAccount.getType() == EnumTypeBankAccount.FIXEDTERM && !movements.isEmpty()) {
                        return Mono.error(new MovementLimitOneMonthException("The client can only make one movement per month"));
                    }
                    return Mono.just(true);
                });
    }

    private Mono<Movement> processMovement(MovementDto movementDto, BankAccount bankAccount) {
        if (movementDto.getType() == EnumTypeMovement.DEPOSIT) {
            bankAccount.setBalance(bankAccount.getBalance() + movementDto.getAmount());
        } else if (movementDto.getType() == EnumTypeMovement.WITHDRAWAL) {
            if (bankAccount.getBalance() < movementDto.getAmount()) {
                return Mono.error(new InsufficientFundsException("Insufficient funds for withdrawal"));
            }
            bankAccount.setBalance(bankAccount.getBalance() - movementDto.getAmount());
        }

        return movementRepository
                .save(movementDtoToMovement(movementDto))
                .flatMap(movement -> bankAccountRepository.save(bankAccount).thenReturn(movement));
    }

    private Mono<CustomerGetDto> getCustomerGetDto(String customer) {
        return webClient
                .build()
                .get()
                .uri("http://localhost:8090/api/customers/{id}", customer)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new CustomerNotFoundException("Customer not found")))
                .bodyToMono(CustomerGetDto.class)
                .flatMap(Mono::just);
    }

    private Movement movementDtoToMovement(MovementDto movementDto) {
        Movement movement = new Movement();
        movement.setBankAccount(movementDto.getBankAccount());
        movement.setCustomer(movementDto.getCustomer());
        movement.setType(movementDto.getType());
        movement.setAmount(movementDto.getAmount());
        movement.setDateCreated(new Date());
        movement.setDescription(movementDto.getDescription());

        return movement;
    }

    private MovementGetDto movementToMovementGetDto(Movement movement) {
        MovementGetDto movementGetDto = new MovementGetDto();
        movementGetDto.setId(movement.getId());
        movementGetDto.setProduct(movement.getBankAccount());
        movementGetDto.setCustomer(movement.getCustomer());
        movementGetDto.setType(movement.getType());
        movementGetDto.setAmount(movement.getAmount());
        movementGetDto.setDateCreated(new Date());
        movementGetDto.setDescription(movement.getDescription());

        return movementGetDto;
    }

    private Date getStartOfMonth() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        return Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date getEndOfMonth() {
        LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return Date.from(lastDayOfMonth.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
    }
}