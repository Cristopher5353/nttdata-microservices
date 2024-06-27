package com.customermicroservice.customer_microservice.models;

import com.customermicroservice.customer_microservice.util.EnumTypeCustomer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    private String name;
    private String lastName;
    private Integer age;
    private EnumTypeCustomer type;
}