package com.adamludzia.controller;

import com.adamludzia.model.Invoice;
import com.adamludzia.service.RestService;
import java.util.List;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Controller implements InvApi {

    private final RestService restService;
    
    @Override
    public long add(@RequestBody Invoice invoice) {
        return restService.save(invoice);
    }

    @Override
    public List<Invoice> getAll() {
        return restService.getAll();
    }

    @Override
    public ResponseEntity<Invoice> getById(@PathVariable int id) {
        return restService.getById(id);
    }

    @Override
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Invoice invoice) {
        return restService.update(id, invoice);
    }

    @Override
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        return restService.deleteById(id);
    }

}
