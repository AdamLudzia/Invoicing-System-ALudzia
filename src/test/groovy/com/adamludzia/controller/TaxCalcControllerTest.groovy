package com.adamludzia.controller

import com.adamludzia.model.Vat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import com.adamludzia.model.Car
import com.adamludzia.model.Company
import com.adamludzia.model.Invoice
import com.adamludzia.model.InvoiceEntry
import com.adamludzia.model.Vat
import com.adamludzia.service.JsonService
import com.adamludzia.service.TaxCalcResult
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static com.adamludzia.TestHelpersTest.invoice

@AutoConfigureMockMvc
@SpringBootTest
@Unroll
class TaxCalcControllerTest extends Specification{

    static final String INVOICE_ENDPOINT = "/invoices"
    static final String TAX_CALCULATOR_ENDPOINT = "/tax"

    @Autowired
    MockMvc mockMvc

    @Autowired
    JsonService jsonService

    def setup() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
    }

    def "zeros are returned when there are no invoices in the system"() {
        when:
        def taxCalcResponse = calculateTax(company(0))

        then:
        taxCalcResponse.income == 0
        taxCalcResponse.costs == 0
        taxCalcResponse.earnings == 0
        taxCalcResponse.incomingVat == 0
        taxCalcResponse.outgoingVat == 0
        taxCalcResponse.vatToReturn == 0
    }

    def "zeros are returned when tax id is not matching"() {
        given:
        addUniqueInvoices(10)

        when:
        def taxCalcResponse = calculateTax(company(-1))

        then:
        taxCalcResponse.income == 0
        taxCalcResponse.costs == 0
        taxCalcResponse.earnings == 0
        taxCalcResponse.incomingVat == 0
        taxCalcResponse.outgoingVat == 0
        taxCalcResponse.vatToReturn == 0
    }

    def "sum of all products is returned when tax id is matching"() {
        given:
        addUniqueInvoices(10)

        when:
        def taxCalcResponse = calculateTax(company(5))

        then:
        taxCalcResponse.income == 30000
        taxCalcResponse.costs == 0
        taxCalcResponse.earnings == 30000
        taxCalcResponse.incomingVat == 2400.0
        taxCalcResponse.outgoingVat == 0
        taxCalcResponse.vatToReturn == 2400.0

        when:
        taxCalcResponse = calculateTax(company(10))

        then:
        taxCalcResponse.income == 110000
        taxCalcResponse.costs == 0
        taxCalcResponse.earnings == 110000
        taxCalcResponse.incomingVat == 8800.0
        taxCalcResponse.outgoingVat == 0
        taxCalcResponse.vatToReturn == 8800.0

        when:
        taxCalcResponse = calculateTax(company(15))

        then:
        taxCalcResponse.income == 0
        taxCalcResponse.costs == 30000
        taxCalcResponse.earnings == -30000
        taxCalcResponse.incomingVat == 0
        taxCalcResponse.outgoingVat == 2400.0
        taxCalcResponse.vatToReturn == -2400.0
    }

    def "correct values are returned when company was buyer and seller"() {
        given:
        addUniqueInvoices(15) // sellers: 1-15, buyers: 10-25, 10-15 overlapping

        when:
        def taxCalcResponse = calculateTax(company(12))

        then:
        taxCalcResponse.income == 156000
        taxCalcResponse.costs == 6000
        taxCalcResponse.earnings == 150000
        taxCalcResponse.incomingVat == 12480.0
        taxCalcResponse.outgoingVat == 480.0
        taxCalcResponse.vatToReturn == 12000.0
    }

    def "tax is calculated correctly when car is not used for personal purposes"() {
        given:
        def invoice = Invoice.builder()
                .date(LocalDate.now())
                .number("9999")
                .seller(company(1))
                .buyer(company(2))
                .entries(List.of(
                        InvoiceEntry.builder()
                                .vatValue(BigDecimal.valueOf(23.45))
                                .vatRate(Vat.VAT_8)
                                .price(BigDecimal.valueOf(100))
                                .quantity(1.0)                        
                                .carExpenses(
                                        Car.builder()
                                                .personalUsage(A)
                                                .registrationPlate("GA 55997")
                                                .build()
                                )
                                .build()
                ))
                .build()

        addInvoiceAndReturnId(invoice)

        when:
        def taxCalcResponse = calculateTax(invoice.getSeller())

        then: "no proportion - it applies only when you are the buyer"
        taxCalcResponse.income == income
        taxCalcResponse.costs == costs
        taxCalcResponse.earnings == earnings
        taxCalcResponse.incomingVat == iVat
        taxCalcResponse.outgoingVat == oVat
        taxCalcResponse.vatToReturn == vToReturn


        when:
        taxCalcResponse = calculateTax(invoice.getBuyer())

        then: "proportion applied - it applies when you are the buyer"
        taxCalcResponse.income == income2
        taxCalcResponse.costs == costs2
        taxCalcResponse.earnings == earnings2
        taxCalcResponse.incomingVat == iVat2
        taxCalcResponse.outgoingVat == oVat2
        taxCalcResponse.vatToReturn == vToReturn2

        where:
        A       ||   income  |   costs   |   earnings    |   iVat    |   oVat    |   vToReturn |   income2 |   costs2  |   earnings2   |   iVat2   |   oVat2   |   vToReturn2
        true    ||   100     |   0       |   100         |   23.45   |   0       |   23.45     |   0       |   111.73  |   -111.73     |   0       |   23.45   |   -23.45
        false   ||   100     |   0       |   100         |   23.45   |   0       |   23.45     |   0       |   100     |   -100        |   0       |   23.45   |   -23.45


    }

    def "All calculations are executed correctly"() {
        given:
        def ourCompany = Company.builder()
                .taxIdentificationNumber("1234")
                .address("uber")
                .name("cost 10 zł")
                .pensionInsurance(514.57)
                .healthInsurance(319.94)
                .build()

        def invoiceWithIncome = Invoice.builder()
                .date(LocalDate.now())
                .number("guess qho")
                .seller(ourCompany)
                .buyer(company(2))
                .entries(List.of(
                        InvoiceEntry.builder()
                                .price(76011.62)
                                .vatValue(0.0)
                                .quantity(1.0)
                                .vatRate(Vat.VAT_0)
                                .build()
                ))
                .build()

        def invoiceWithCosts = Invoice.builder()
                .date(LocalDate.now())
                .number("guess who")
                .seller(company(4))
                .buyer(ourCompany)
                .entries(List.of(
                        InvoiceEntry.builder()
                                .price(11329.47)
                                .vatValue(0.0)
                                .quantity(1.0)
                                .vatRate(Vat.VAT_0)
                                .build()
                ))
                .build()

        addInvoiceAndReturnId(invoiceWithIncome)
        addInvoiceAndReturnId(invoiceWithCosts)

        when:
        def taxCalculatorResponse = calculateTax(ourCompany)

        then:
        with(taxCalculatorResponse) {
            income == 76011.62
            costs == 11329.47
            earnings == 64682.15
            pensionInsurance == 514.57
            earningsSubPensionInsurance == 64167.58
            earningsSubPensionInsuranceRounded == 64168
            incomeTax == 12191.92
            healthInsuranceReference == 319.94
            healthInsuranceReduce == 275.50
            incomeTaxMinusHealthInsurance == 11916.42
            finalIncomeTax == 11916

            incomingVat == 0
            outgoingVat == 0
            vatToReturn == 0
        }
    }

    TaxCalcResult calculateTax(Company company) {
        def response = mockMvc.perform(
                post("$TAX_CALCULATOR_ENDPOINT")
                        .content(jsonService.objectAsJson(company))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.returnJsonAsInvoice(response, TaxCalcResult)
    }

    List<Invoice> addUniqueInvoices(long count) {
        (1..count).collect { id ->
            def invoice = invoice(id)
            invoice.id = addInvoiceAndReturnId(invoice)
            invoice
        }
    }

    long addInvoiceAndReturnId(Invoice invoice) {
        Integer.valueOf(
                mockMvc.perform(
                        post(INVOICE_ENDPOINT)
                                .content(jsonService.objectAsJson(invoice))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andReturn()
                        .response
                        .contentAsString
        )
    }

    List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get(INVOICE_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.returnJsonAsInvoice(response, Invoice[])
    }

    void deleteInvoice(long id) {
        mockMvc.perform(delete("$INVOICE_ENDPOINT/$id"))
                .andExpect(status().isNoContent())
    }

}
