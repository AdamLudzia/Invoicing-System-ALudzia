package com.adamludzia.service;

import com.adamludzia.db.Database;
import com.adamludzia.model.Company;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final Database<Company> database;

    public CompanyService(Database<Company> database) {
        this.database = database;
    }

    public long save(Company company) {
        return database.save(company);
    }

    public Optional<Company> getById(long id) throws SQLException {
        return database.getById(id);
    }

    public List<Company> getAll() throws SQLException {
        return database.getAll();
    }

    public Optional<Company> update(long id, Company updatedCompany) throws SQLException {
        updatedCompany.setId(id); // just in case it was not set
        return database.update(id, updatedCompany);
    }

    public Optional<Company> delete(long id) throws SQLException {
        return database.delete(id);
    }

}
