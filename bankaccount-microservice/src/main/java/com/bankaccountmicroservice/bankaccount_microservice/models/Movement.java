package com.bankaccountmicroservice.bankaccount_microservice.models;

import com.bankaccountmicroservice.bankaccount_microservice.util.EnumTypeMovement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movements")
public class Movement {
    @Id
    private String id;
    private String bankAccount;
    private String customer;
    private EnumTypeMovement type;
    private Double amount;
    private Date dateCreated;
    private String description;
}