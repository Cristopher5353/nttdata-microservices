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
public class MovementGetDto {
    private String id;
    private String product;
    private String customer;
    private EnumTypeMovement type;
    private Double amount;
    private Date dateCreated;
    private String description;
}