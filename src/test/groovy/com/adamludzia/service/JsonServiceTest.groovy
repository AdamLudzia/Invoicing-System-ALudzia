package com.adamludzia.service

import com.adamludzia.TestHelpersTest
import com.adamludzia.model.Invoice

import spock.lang.Specification

class JsonServiceTest extends Specification {
    def "can convert object to json and read it back"() {
        given:
        def jsonService = new JsonService()
        def invoice = TestHelpersTest.invoice(12)

        when:
        def invoiceAsString = jsonService.invoiceAsJson(invoice)
        System.out.println(invoiceAsString)
        and:
        def invoiceFromJson = jsonService.returnJsonAsInvoice(invoiceAsString, Invoice.class)

        then:
        invoice == invoiceFromJson
    }
}
