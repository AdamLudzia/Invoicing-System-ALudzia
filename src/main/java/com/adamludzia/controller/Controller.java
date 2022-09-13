package com.adamludzia.controller;

import com.adamludzia.model.Invoice;
import com.adamludzia.service.InvoiceRestService;
import java.sql.SQLException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Controller implements InvApi {

    private final InvoiceRestService restService;
    
    @Override
    public long add(@RequestBody Invoice invoice) {
        return restService.save(invoice);
    }

    @Override
    public List<Invoice> getAll() throws SQLException {
        return restService.getAll();
    }

    @Override
    public ResponseEntity<Invoice> getById(@PathVariable int id) throws SQLException {
        return restService.getById(id);
    }

    @Override
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Invoice invoice) throws SQLException {
        return restService.update(id, invoice);
    }

    @Override
    public ResponseEntity<?> deleteById(@PathVariable int id) throws SQLException {
        return restService.deleteById(id);
    }

}
