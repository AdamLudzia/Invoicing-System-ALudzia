package com.adamludzia.db.impl;

import com.adamludzia.db.Database;
import com.adamludzia.model.Company;
import com.adamludzia.model.Invoice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "jpa")
public class JpaDatabaseConfiguration {

    @Bean
    public Database<Invoice> invoiceJpaDatabase(InvoiceRepository repository) {
        return new JpaDatabase<>(repository);
    }

    @Bean
    public Database<Company> companyJpaDatabase(CompanyRepository repository) {
        return new JpaDatabase<>(repository);
    }

}
