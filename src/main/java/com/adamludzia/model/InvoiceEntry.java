package com.adamludzia.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Entity
public class InvoiceEntry {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Invoice entry id (generated by application)", required = true, example = "1")
    private long id;

    @ApiModelProperty(value = "Product/service description", required = true, example = "Odkurzanie")
    private String description;

    @ApiModelProperty(value = "Number of items", required = true, example = "1")
    private BigDecimal quantity;

    @ApiModelProperty(value = "Product/service net price", required = true, example = "100.00")
    private BigDecimal price;

    @ApiModelProperty(value = "Product/service tax value", required = true, example = "20.99")
    @Builder.Default
    private BigDecimal vatValue = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "Tax rate", required = true)
    private Vat vatRate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @ApiModelProperty(value = "Expenses that is related to car")
    private Car carExpenses;
    
}
