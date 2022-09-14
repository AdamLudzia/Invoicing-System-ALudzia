package com.adamludzia.controller;

import com.adamludzia.model.Company;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.sql.SQLException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "companies", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"company-controller"})
public interface CompanyApi {

    @ApiOperation(value = "Get list of all companies")
    @GetMapping
    List<Company> getAll() throws SQLException;

    @ApiOperation(value = "Add new company to system")
    @PostMapping
    long add(@RequestBody Company company);

    @ApiOperation(value = "Get company by id")
    @GetMapping(value = "/{id}")
    ResponseEntity<Company> getById(@PathVariable long id) throws SQLException;

    @ApiOperation(value = "Delete company with given id")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable long id) throws SQLException;

    @ApiOperation(value = "Update company with given id")
    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable long id, @RequestBody Company company) throws SQLException;
}
