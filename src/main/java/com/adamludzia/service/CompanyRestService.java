package com.adamludzia.service;

import com.adamludzia.db.Database;
import com.adamludzia.model.Company;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CompanyRestService {

    private Database<Company> database;

    @Autowired
    public void setDatabase(Database<Company> database) {
        this.database = database;
    }

    public long save(Company company) {
        return database.save(company);
    }

    public ResponseEntity<Company> getById(long id) throws SQLException {
        return database.getById(id)
            .map(company -> ResponseEntity.ok().body(company))
            .orElse(ResponseEntity.notFound().build());
    }

    public List<Company> getAll() throws SQLException {
        return database.getAll();
    }

    public ResponseEntity<?> update(long id, Company company) throws SQLException {
        return database.update(id, company)
            .map(name -> ResponseEntity.noContent().build())
            .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteById(long id) throws SQLException {
        return database.delete(id)
            .map(name -> ResponseEntity.noContent().build())
            .orElse(ResponseEntity.notFound().build());
    }
}
