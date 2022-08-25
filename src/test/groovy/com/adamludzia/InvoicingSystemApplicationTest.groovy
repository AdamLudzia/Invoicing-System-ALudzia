package com.adamludzia

import com.adamludzia.service.InvoiceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class InvoicingSystemApplicationTest extends Specification {
    @Autowired
    private InvoiceService invoiceService

    def "invoice service is created"() {
        expect:
        invoiceService
    }
}
