package com.productmicroservice.product_microservice.models;

import com.productmicroservice.product_microservice.util.EnumTypeHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

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