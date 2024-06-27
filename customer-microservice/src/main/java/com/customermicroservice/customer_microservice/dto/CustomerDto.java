package com.customermicroservice.customer_microservice.dto;

import com.customermicroservice.customer_microservice.util.EnumTypeCustomer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    @NotBlank(message = "The name field must not be empty")
    private String name;
    @NotBlank
    @NotBlank(message = "The last name field must not be empty")
    private String lastName;
    @NotNull(message = "The age field must not be empty")
    @Min(value = 0)
    private Integer age;
    @NotNull
    @NotNull(message = "The type field must not be empty")
    private EnumTypeCustomer type;
}