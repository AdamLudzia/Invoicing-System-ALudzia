package com.adamludzia.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Company {

    private String id;
    @ApiModelProperty(value = "Tax identification number", required = true, example = "111-222-33-44")
    private String taxIdentificationNumber;

    @ApiModelProperty(value = "Company address and name", required = true, example = "ul. Wodnika Szuwarka 28, 00-001 Warszawa, Debesciak Sp. z o.o.")
    private String address;

    public Company(String id, String taxIdentificationNumber, String address) {
        this.id = id;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.address = address;
    }
}
