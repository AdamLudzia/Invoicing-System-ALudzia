package com.adamludzia.service;

import com.adamludzia.db.Database;
import com.adamludzia.model.Invoice;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class InvoiceRestService {

    private Database<Invoice> database;

    @Autowired
    public void setDatabase(Database<Invoice> database) {
        this.database = database;
    }

    public long save(Invoice invoice) {
        return database.save(invoice);
    }

    public ResponseEntity<Invoice> getById(long id) throws SQLException {
        return database.getById(id)
            .map(invoice -> ResponseEntity.ok().body(invoice))
            .orElse(ResponseEntity.notFound().build());
    }

    public List<Invoice> getAll() throws SQLException {
        return database.getAll();
    }

    public ResponseEntity<?> update(long id, Invoice invoice) throws SQLException {
        return database.update(id, invoice)
            .map(name -> ResponseEntity.noContent().build())
            .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteById(long id) throws SQLException {
        return database.delete(id)
            .map(name -> ResponseEntity.noContent().build())
            .orElse(ResponseEntity.notFound().build());
    }
}
