package com.bankaccountmicroservice.bankaccount_microservice.dto;

import com.bankaccountmicroservice.bankaccount_microservice.util.EnumTypeMovement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovementDto {
    private String customer;
    private String bankAccount;
    private Double amount;
    private EnumTypeMovement type;
    private Date dateCreated;
    private String description;
}
