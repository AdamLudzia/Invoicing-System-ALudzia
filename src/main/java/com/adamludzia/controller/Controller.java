package com.adamludzia.controller;

import com.adamludzia.model.Invoice;
import com.adamludzia.service.RestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("invoices")
public class Controller {

    @Autowired
    private final RestService restService;

    @Autowired
    public Controller(RestService restService) {
        this.restService = restService;
    }

    @PostMapping
    public int add(@RequestBody Invoice invoice) {
        return restService.save(invoice);
    }

    @GetMapping
    public List<Invoice> getAll() {
        return restService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getById(@PathVariable int id) {
        return restService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Invoice invoice) {
        return restService.update(id, invoice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        return restService.deleteById(id);
    }

}
