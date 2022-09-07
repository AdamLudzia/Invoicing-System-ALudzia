package com.adamludzia.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class InvoiceEntry {

    @ApiModelProperty(value = "Product/service description", required = true, example = "Odkurzanie")
    private String description;

    @ApiModelProperty(value = "Number of items", required = true, example = "1")
    private int quantity;

    @ApiModelProperty(value = "Product/service net price", required = true, example = "100.00")
    private BigDecimal price;

    @ApiModelProperty(value = "Product/service tax value", required = true, example = "20.99")
    private BigDecimal vatValue;

    @ApiModelProperty(value = "Tax rate", required = true)
    private Vat vatRate;

    public InvoiceEntry(String description, int quantity, BigDecimal price, BigDecimal vatValue, Vat vatRate) {
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.vatValue = vatValue;
        this.vatRate = vatRate;
    }

}
