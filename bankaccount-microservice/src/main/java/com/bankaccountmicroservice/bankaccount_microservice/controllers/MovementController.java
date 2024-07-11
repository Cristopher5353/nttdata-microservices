package com.bankaccountmicroservice.bankaccount_microservice.controllers;

import com.bankaccountmicroservice.bankaccount_microservice.dto.MovementDto;
import com.bankaccountmicroservice.bankaccount_microservice.dto.MovementGetDto;
import com.bankaccountmicroservice.bankaccount_microservice.services.IMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bank-accounts")
public class MovementController {
    @Autowired
    private IMovementService iMovementService;

    @PostMapping("/movements")
    public Mono<MovementGetDto> movement(@RequestBody MovementDto movementDto) {
        return iMovementService.movement(movementDto);
    }
}