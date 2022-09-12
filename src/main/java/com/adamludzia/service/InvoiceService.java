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

    public long save(Invoice invoice) {
        return database.save(invoice);
    }

    public Optional<Invoice> getById(long id) {
        return database.getById(id);
    }

    public List<Invoice> getAll() {
        return database.getAll();
    }

    public Optional<Invoice> update(long id, Invoice updatedInvoice) {
        return database.update(id, updatedInvoice);
    }

    public Optional<Invoice> delete(long id) {
        return database.delete(id);
    }

}
