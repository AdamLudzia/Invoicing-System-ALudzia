package com.adamludzia.service;

import com.adamludzia.db.Database;
import com.adamludzia.model.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private Database database;

    public InvoiceService(Database database) {
        this.database = database;
    }

    @Autowired
    public void setDatabase(Database database) {
        this.database = database;
    }

    public int save(Invoice invoice) {
        return database.save(invoice);
    }

    public Optional<Invoice> getById(int id) {
        return database.getById(id);
    }

    public List<Invoice> getAll() {
        return database.getAll();
    }

    public Optional<Invoice> update(int id, Invoice updatedInvoice) {
        return database.update(id, updatedInvoice);
    }

    public Optional<Invoice> delete(int id) {
        return database.delete(id);
    }

}
