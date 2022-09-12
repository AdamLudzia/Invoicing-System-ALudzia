package com.adamludzia.controller;

import com.adamludzia.model.Company;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.adamludzia.service.TaxCalcResult;
import com.adamludzia.service.TaxCalcService;

@RestController
@AllArgsConstructor
public class TaxCalcController implements TaxCalcApi {

    private final TaxCalcService taxCalcService;

    @Override
    public TaxCalcResult calculateTaxes(@RequestBody Company company) {
        return taxCalcService.calculateTaxes(company);
    }
}
