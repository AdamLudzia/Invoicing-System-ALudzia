package com.adamludzia.db.impl

import com.adamludzia.db.Database
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.IfProfileValue


@DataJpaTest
@IfProfileValue(name = "spring.profiles.active", value = "jpa")
class JpaDatabaseTest extends AbstractDatabaseTest {

    @Autowired
    private InvoiceRepository invoiceRepository

    @Override
    Database getDatabaseInstance() {
        assert invoiceRepository != null
        new JpaDatabase(invoiceRepository)
    }

}
