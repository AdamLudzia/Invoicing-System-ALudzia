package com.adamludzia.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Company {

    private String id;
    @ApiModelProperty(value = "Tax identification number", required = true, example = "111-222-33-44")
    private String taxIdentificationNumber;

    @ApiModelProperty(value = "Company address and name", required = true, example = "ul. Wodnika Szuwarka 28, 00-001 Warszawa, Debesciak Sp. z o.o.")
    private String address;

    @ApiModelProperty(value = "Company name", required = true, example = "Debesciak Sp. z o.o.")
    private String name;
}
