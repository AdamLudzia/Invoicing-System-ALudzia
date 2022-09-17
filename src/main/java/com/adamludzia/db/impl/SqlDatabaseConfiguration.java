package com.adamludzia.db.impl;

import com.adamludzia.db.Database;
import com.adamludzia.model.Company;
import com.adamludzia.model.Invoice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "sql")
public class SqlDatabaseConfiguration {

    @Bean
    public Database<Invoice> invoiceSqlDatabase(JdbcTemplate jdbcTemplate) {
        return new InvoiceSqlDatabase(jdbcTemplate);
    }

    @Bean
    public Database<Company> companySqlDatabase(JdbcTemplate jdbcTemplate) {
        return new CompanySqlDatabase(jdbcTemplate);
    }

}
