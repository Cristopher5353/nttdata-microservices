package com.productmicroservice.product_microservice.models;

import com.productmicroservice.product_microservice.util.EnumTypeCredit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "credits")
public class Credit {
    private String id;
    private Date dateCreated;
    private Double balance;
    private Integer limit;
    private EnumTypeCredit type;
    private String customer;
}