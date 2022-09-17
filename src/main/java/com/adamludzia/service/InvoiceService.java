package com.adamludzia.service;

import com.adamludzia.db.Database;
import com.adamludzia.model.Invoice;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private Database<Invoice> database;

    public InvoiceService(Database<Invoice> database) {
        this.database = database;
    }

    @Autowired
    public void setDatabase(Database<Invoice> database) {
        this.database = database;
    }

    public long save(Invoice invoice) {
        return database.save(invoice);
    }

    public Optional<Invoice> getById(long id) throws SQLException {
        return database.getById(id);
    }

    public List<Invoice> getAll() throws SQLException {
        return database.getAll();
    }

    public Optional<Invoice> update(long id, Invoice updatedInvoice) throws SQLException {
        updatedInvoice.setId(id);
        return database.update(id, updatedInvoice);
    }

    public Optional<Invoice> delete(long id) throws SQLException {
        return database.delete(id);
    }

}
