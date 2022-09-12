package com.adamludzia.db;

import com.adamludzia.model.Invoice;
import com.adamludzia.model.InvoiceEntry;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Database {

    int save(Invoice invoice);

    Optional<Invoice> getById(int id);

    List<Invoice> getAll();

    Optional<Invoice> update(int id, Invoice updatedInvoice);

    Optional<Invoice> delete(int id);
    
    default BigDecimal visit(Predicate<Invoice> invoicePredicate, Function<InvoiceEntry, BigDecimal> invoiceEntryToValue) {
        return getAll().stream()
            .filter(invoicePredicate)
            .flatMap(i -> i.getEntries().stream())
            .map(invoiceEntryToValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
