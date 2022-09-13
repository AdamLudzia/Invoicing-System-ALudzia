package com.adamludzia.db.impl;

import com.adamludzia.db.Database;
import com.adamludzia.model.Company;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.flywaydb.core.internal.jdbc.RowMapper;

public class CompanySqlDatabase extends AbstractSqlDatabase implements Database<Company> {

    public static final String SELECT_QUERY = "select * from company";

    public CompanySqlDatabase(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    @Transactional
    public long save(Company company) {
        return insertCompany(company);
    }

    @Override
    public List<Company> getAll() throws SQLException {
        return jdbcTemplate.query(SELECT_QUERY, companyRowMapper());
    }

    @Override
    public Optional<Company> getById(long id) throws SQLException {
        List<Company> companies = jdbcTemplate.query(SELECT_QUERY + " where id = " + id, companyRowMapper());

        return companies.isEmpty() ? Optional.empty() : Optional.of(companies.get(0));
    }

    @Override
    @Transactional
    public Optional<Company> update(long id, Company updatedCompany) throws SQLException {
        Optional<Company> originalCompany = getById(id);
        if (originalCompany.isEmpty()) {
            return Optional.empty();
        }

        updateCompany(updatedCompany);

        return originalCompany;
    }

    @Override
    @Transactional
    public Optional<Company> delete(long id) throws SQLException {
        Optional<Company> originalCompany = getById(id);
        if (originalCompany.isEmpty()) {
            return Optional.empty();
        }

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "delete from company where id = ?;");
            ps.setLong(1, id);
            return ps;
        });

        return originalCompany;
    }

    private RowMapper<Company> companyRowMapper() {
        return (rs) ->
            Company.builder()
                .id(rs.getLong("id"))
                .taxIdentificationNumber(rs.getString("tax_identification_number"))
                .name(rs.getString("name"))
                .address(rs.getString("address"))
                .pensionInsurance(rs.getBigDecimal("pension_insurance"))
                .healthInsurance(rs.getBigDecimal("health_insurance"))
                .build();
    }

}
