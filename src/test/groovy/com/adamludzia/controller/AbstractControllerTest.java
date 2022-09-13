package com.adamludzia.controller;

import static com.adamludzia.TestHelpersTest.company;
import static com.adamludzia.TestHelpersTest.invoice;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.adamludzia.model.Company;
import com.adamludzia.model.Invoice;
import com.adamludzia.service.JsonService;
import com.adamludzia.service.TaxCalcResult;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import spock.lang.Specification;






@AutoConfigureMockMvc
@SpringBootTest
public class AbstractControllerTest extends Specification {
    static final String INVOICE_ENDPOINT = "/invoices";
    static final String COMPANY_ENDPOINT = "/companies";
    static final String TAX_CALCULATOR_ENDPOINT = "/tax";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JsonService jsonService;

    int addInvoiceAndReturnId(Invoice invoice) {
        addAndReturnId(invoice, INVOICE_ENDPOINT);
    }

    int addCompanyAndReturnId(Company company) {
        addAndReturnId(company, COMPANY_ENDPOINT);
    }

    List<Invoice> getAllInvoices() {
        getAll(Invoice[], INVOICE_ENDPOINT)
    }

    List<Company> getAllCompanies() {
        getAll(Company[], COMPANY_ENDPOINT)
    }

    List<Invoice> addUniqueInvoices(int count) {
        (1..count).collect { id ->
            def invoice = invoice(id);
            invoice.id = addInvoiceAndReturnId(invoice);
            invoice
        }
    }

    List<Company> addUniqueCompanies(int count) {
        (1..count).collect { id ->
            def company = company(id);
            company.id = addCompanyAndReturnId(company);
            company
        }
    }

    void deleteInvoice(long id) {
        mockMvc.perform(delete("$INVOICE_ENDPOINT/$id"))
            .andExpect(status().isNoContent());
    }

    void deleteCompany(long id) {
        mockMvc.perform(delete("$COMPANY_ENDPOINT/$id"))
            .andExpect(status().isNoContent());
    }

    Company getCompanyById(long id) {
        getById(id, Company, COMPANY_ENDPOINT);
    }

    String companyAsJson(long id) {
        jsonService.objectAsJson(company(id));
    }

    TaxCalcResult calculateTax(Company company) {
        def response = mockMvc.perform(
                post(TAX_CALCULATOR_ENDPOINT)
                    .content(jsonService.objectAsJson(company))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn()
            .response
            .contentAsString;

        jsonService.returnJsonAsObject(response, TaxCalcResult);
    }

    private <T> int addAndReturnId(T item, String endpoint) {
        Integer.valueOf(
            mockMvc.perform(
                    post(endpoint)
                        .content(jsonService.objectAsJson(item))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        )
    }

    private <T> T getAll(Class<T> clazz, String endpoint) {
        def response = mockMvc.perform(get(endpoint))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .response
            .contentAsString;

        jsonService.returnJsonAsObject(response, clazz)
    }

    private <T> T getById(long id, Class<T> clazz, String endpoint) {
        def invoiceAsString = mockMvc.perform(get("$endpoint/$id"))
            .andExpect(status().isOk())
            .andReturn()
            .response
            .contentAsString;

        jsonService.returnJsonAsObject(invoiceAsString, clazz)
    }

}}
