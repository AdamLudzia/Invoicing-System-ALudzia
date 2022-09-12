package com.adamludzia.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import com.adamludzia.service.TaxCalcResult;
import com.adamludzia.service.TaxCalcService;

@RestController
@AllArgsConstructor
public class TaxCalcController implements TaxCalcApi {

    private final TaxCalcService taxCalculatorService;

    @Override
    public TaxCalcResult calculateTaxes(String taxIdentificationNumber) {
        return taxCalculatorService.calculateTaxes(taxIdentificationNumber);
    }

}
