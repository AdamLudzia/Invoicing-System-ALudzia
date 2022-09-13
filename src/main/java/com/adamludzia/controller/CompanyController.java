package com.adamludzia.controller;

import com.adamludzia.model.Company;
import com.adamludzia.service.CompanyRestService;
import java.sql.SQLException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CompanyController implements CompanyApi {

    private final CompanyRestService restService;

    @Override
    public List<Company> getAll() throws SQLException {
        return restService.getAll();
    }

    @Override
    public long add(@RequestBody Company company) {
        return restService.save(company);
    }

    @Override
    public ResponseEntity<Company> getById(@PathVariable long id) throws SQLException {
        return restService.getById(id);
    }

    @Override
    public ResponseEntity<?> deleteById(@PathVariable long id) throws SQLException {
        return restService.deleteById(id);
    }

    @Override
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody Company company) throws SQLException {
        return restService.update(id, company);
    }

}
