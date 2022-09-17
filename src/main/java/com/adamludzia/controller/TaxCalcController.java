package com.adamludzia.controller;

import com.adamludzia.model.Company;
import com.adamludzia.service.TaxCalcResult;
import com.adamludzia.service.TaxCalcService;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TaxCalcController implements TaxCalcApi {

    @Autowired
    private final TaxCalcService taxCalcService;

    @Override
    public TaxCalcResult calculateTaxes(@RequestBody Company company) throws SQLException {
        return taxCalcService.calculateTaxes(company);
    }
}
