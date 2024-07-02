package com.productmicroservice.product_microservice.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String name;
    private String lastName;
    private Integer age;
    private EnumTypeCustomer type;
}