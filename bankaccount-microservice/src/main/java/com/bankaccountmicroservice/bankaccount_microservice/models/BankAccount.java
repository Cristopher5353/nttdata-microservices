package com.bankaccountmicroservice.bankaccount_microservice.models;

import com.bankaccountmicroservice.bankaccount_microservice.util.EnumTypeBankAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bank_accounts")
public class BankAccount {
    @Id
    private String id;
    private String number;
    private Date dateCreated;
    private Double balance;
    private Integer limit;
    private EnumTypeBankAccount type;
    private String customer;
    private List<Holder> holders;
}