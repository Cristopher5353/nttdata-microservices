package com.bankaccountmicroservice.bankaccount_microservice.dto;

import com.bankaccountmicroservice.bankaccount_microservice.util.EnumTypeCustomer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGetDto {
    private String id;
    private String name;
    private String lastName;
    private Integer age;
    private EnumTypeCustomer type;
}