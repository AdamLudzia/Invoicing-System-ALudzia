package com.adamludzia.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceEntry {

    private String description;
    private int quantity;
    private BigDecimal price;
    private BigDecimal vatValue;
    private Vat vatRate;

}
