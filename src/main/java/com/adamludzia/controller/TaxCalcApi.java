package com.adamludzia.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.adamludzia.service.TaxCalcResult;

@RequestMapping("tax")
@Api(tags = {"tax-controller"})
public interface TaxCalcApi {

    @ApiOperation(value = "Get the value of taxes")
    @GetMapping(value = "/{taxIdentificationNumber}", produces = {"application/json;charset=UTF-8"})
    TaxCalcResult calculateTaxes(@PathVariable @ApiParam(example = "999-666-00-12") String taxIdentificationNumber);

}
