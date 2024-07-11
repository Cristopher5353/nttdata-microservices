package com.bankaccountmicroservice.bankaccount_microservice.models;

import com.bankaccountmicroservice.bankaccount_microservice.util.EnumTypeHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Holder {
    private String name;
    private String lastName;
    private String document;
    private EnumTypeHolder type;
}