package com.adamludzia.controller;

import com.adamludzia.model.Invoice;
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


@RequestMapping("invoices")
@Api(tags = {"invoice-controller"})
public interface InvApi {

    @ApiOperation(value = "Add new invoice to system")
    @PostMapping
    long add(@RequestBody Invoice invoice);

    @ApiOperation(value = "Get list of all invoices")
    @GetMapping(produces = {"application/json;charset=UTF-8"})
    List<Invoice> getAll() throws SQLException;

    @ApiOperation(value = "Get invoice by id")
    @GetMapping("/{id}")
    ResponseEntity<Invoice> getById(@PathVariable int id) throws SQLException;

    @ApiOperation(value = "Update invoice with given id")
    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable int id, @RequestBody Invoice invoice) throws SQLException;

    @ApiOperation(value = "Delete invoice with given id")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable int id) throws SQLException;
}
