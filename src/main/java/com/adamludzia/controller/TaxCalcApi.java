package com.adamludzia.controller;

import com.adamludzia.model.Company;
import com.adamludzia.service.TaxCalcResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.sql.SQLException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "tax", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"tax-controller"})
public interface TaxCalcApi {

    @ApiOperation(value = "Get the value of taxes")
    @PostMapping
    TaxCalcResult calculateTaxes(@RequestBody Company company) throws SQLException;

}
