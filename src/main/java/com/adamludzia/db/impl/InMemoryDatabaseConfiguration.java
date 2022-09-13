package com.adamludzia.db.impl;

import com.adamludzia.db.Database;
import com.adamludzia.model.Company;
import com.adamludzia.model.Invoice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
public class InMemoryDatabaseConfiguration {

    @Bean
    public Database<Invoice> invoiceInMemoryDatabase() {
        return new InMemoryDatabase<>();
    }

    @Bean
    public Database<Company> companyInMemoryDatabase() {
        return new InMemoryDatabase<>();
    }

}
