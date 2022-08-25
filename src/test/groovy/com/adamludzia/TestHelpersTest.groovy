package com.adamludzia

import com.adamludzia.model.Company
import com.adamludzia.model.Invoice
import com.adamludzia.model.InvoiceEntry
import com.adamludzia.model.Vat

import java.time.LocalDate

enum TestHelpersTest {
    static company(int id) {
        new Company(("$id").repeat(10),
                "ul. Żelazna 14/$id 81-159 Gdynia, Polska",
                "Idea Solutions $id S.A.")
    }

    static product(int id) {
        new InvoiceEntry("Ship service $id", 1, BigDecimal.valueOf(id * 1000), BigDecimal.valueOf(id * 1000 * 0.08), Vat.VAT_23)
    }

    static invoice(int id) {
        new Invoice(LocalDate.now(), company(id), company(id), List.of(product(id)))
    }

}
