package com.adamludzia

import com.adamludzia.model.Company
import com.adamludzia.model.Invoice
import com.adamludzia.model.InvoiceEntry
import com.adamludzia.model.Vat

import java.time.LocalDate

enum TestHelpersTest {
    static company(int id) {
        Company.builder()
            .taxIdentificationNumber("$id")
            .address("ul. Żelazna 14/$id 81-159 Gdynia, Polska")
            .name("Idea Solutions $id S.A.")
            .pensionInsurance((BigDecimal.TEN * BigDecimal.valueOf(id)).setScale(2))
            .healthInsurance((BigDecimal.valueOf(100) * BigDecimal.valueOf(id)).setScale(2))
            .build()
    }

    static product(int id) {
        InvoiceEntry.builder()
                .description("Antenna matching")
                .quantity(1)
                .price((BigDecimal.valueOf(id * 2000)).setScale(2))
                .vatValue((BigDecimal.valueOf(id * 2000 * 0.08)).setScale(2))
                .vatRate(Vat.VAT_8)
                .build()
    }

    static invoice(int id) {
        Invoice.builder()
                .date(LocalDate.now())
                .number("2022/0101/2564/$id")
                .buyer(company(id + 10))
                .seller(company(id))
                .entries((1..id).collect({ product(it) }))
                .build()
    }

}
