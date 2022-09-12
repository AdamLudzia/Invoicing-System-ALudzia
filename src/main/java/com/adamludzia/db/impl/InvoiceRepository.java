package com.adamludzia.db.impl;

import com.adamludzia.model.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

}
