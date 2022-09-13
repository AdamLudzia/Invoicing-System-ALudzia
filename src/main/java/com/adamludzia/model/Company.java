package com.adamludzia.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Company implements IdInterface {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Company id (generated by application)", required = true, example = "1")
    private long id;
    @ApiModelProperty(value = "Tax identification number", required = true, example = "111-222-33-44")
    private String taxIdentificationNumber;

    @ApiModelProperty(value = "Company address and name", required = true, example = "ul. Wodnika Szuwarka 28, 00-001 Warszawa, Debesciak Sp. z o.o.")
    private String address;

    @ApiModelProperty(value = "Company name", required = true, example = "Debesciak Sp. z o.o.")
    private String name;

    @Builder.Default
    @ApiModelProperty(value = "Pension insurance amount", required = true, example = "1328.75")
    private BigDecimal pensionInsurance = BigDecimal.ZERO;

    @Builder.Default
    @ApiModelProperty(value = "Health insurance amount", required = true, example = "458.34")
    private BigDecimal healthInsurance = BigDecimal.ZERO;

}
