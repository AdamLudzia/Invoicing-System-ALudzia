package com.adamludzia.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
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
    
}
