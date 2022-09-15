package com.adamludzia.service;

import com.adamludzia.model.Car;
import com.adamludzia.model.Company;
import com.adamludzia.db.Database;
import com.adamludzia.model.Invoice;
import com.adamludzia.model.InvoiceEntry;
import java.util.function.Function;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaxCalcService {

    private final Database<Invoice> database;

    public BigDecimal income(String taxIdentificationNumber) throws SQLException {
        return visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getPrice);
    }

    public BigDecimal costs(String taxIdentificationNumber) throws SQLException {
        return visit(buyerPredicate(taxIdentificationNumber),
            this::getIncomeValueTakingIntoConsiderationPersonalCarUsage);
    }

    public BigDecimal incomingVat(String taxIdentificationNumber) throws SQLException {
        return visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getVatValue);
    }

    public BigDecimal outgoingVat(String taxIdentificationNumber) throws SQLException {
        return visit(buyerPredicate(taxIdentificationNumber), InvoiceEntry::getVatValue);
    }

    private BigDecimal getVatValueTakingIntoConsiderationPersonalCarUsage(InvoiceEntry invoiceEntry) {
        return Optional.ofNullable(invoiceEntry.getCarExpenses())
            .map(Car::isPersonalUsage)
            .map(personalCarUsage -> personalCarUsage ? BigDecimal.valueOf(5, 1) : BigDecimal.ONE)
            .map(proportion -> invoiceEntry.getVatValue().multiply(proportion))
            .map(value -> value.setScale(2, RoundingMode.FLOOR))
            .orElse(invoiceEntry.getVatValue());
    }

    private BigDecimal getIncomeValueTakingIntoConsiderationPersonalCarUsage(InvoiceEntry invoiceEntry) {
        return invoiceEntry.getPrice()
            .add(invoiceEntry.getVatValue())
            .subtract(getVatValueTakingIntoConsiderationPersonalCarUsage(invoiceEntry));
    }

    public BigDecimal getEarnings(String taxIdentificationNumber) throws SQLException {
        return income(taxIdentificationNumber).subtract(costs(taxIdentificationNumber));
    }

    public BigDecimal getVatToReturn(String taxIdentificationNumber) throws SQLException {
        return incomingVat(taxIdentificationNumber).subtract(outgoingVat(taxIdentificationNumber));
    }

    public TaxCalcResult calculateTaxes(Company company) throws SQLException {
        String taxIdentificationNumber = company.getTaxIdentificationNumber();

        BigDecimal earnings = getEarnings(taxIdentificationNumber);
        BigDecimal earningsSubPensionInsurance = earnings.subtract(company.getPensionInsurance());
        BigDecimal earningsSubPensionInsuranceRounded = earningsSubPensionInsurance.setScale(0, RoundingMode.HALF_DOWN);
        BigDecimal incomeTax = earningsSubPensionInsuranceRounded.multiply(BigDecimal.valueOf(19, 2));
        BigDecimal healthInsuranceToSubtract =
            company.getHealthInsurance().multiply(BigDecimal.valueOf(775)).divide(BigDecimal.valueOf(900), RoundingMode.HALF_UP);
        BigDecimal finalIncomeTax = incomeTax.subtract(healthInsuranceToSubtract);
        return TaxCalcResult.builder()
            .income(income(taxIdentificationNumber))
            .costs(costs(taxIdentificationNumber))
            .earnings(getEarnings(taxIdentificationNumber))
            .pensionInsurance(company.getPensionInsurance())
            .earningsSubPensionInsurance(earningsSubPensionInsurance)
            .earningsSubPensionInsuranceRounded(earningsSubPensionInsuranceRounded)
            .incomeTax(incomeTax)
            .healthInsuranceReference(company.getHealthInsurance())
            .healthInsuranceReduce(healthInsuranceToSubtract)
            .incomeTaxMinusHealthInsurance(finalIncomeTax)
            .finalIncomeTax(finalIncomeTax.setScale(0, RoundingMode.DOWN))
            .incomingVat(incomingVat(taxIdentificationNumber))
            .outgoingVat(outgoingVat(taxIdentificationNumber))
            .vatToReturn(getVatToReturn(taxIdentificationNumber))
            .build();
    }

    private Predicate<Invoice> sellerPredicate(String taxIdentificationNumber) {
        return invoice -> taxIdentificationNumber.equals(invoice.getSeller().getTaxIdentificationNumber());
    }

    private Predicate<Invoice> buyerPredicate(String taxIdentificationNumber) {
        return invoice -> taxIdentificationNumber.equals(invoice.getBuyer().getTaxIdentificationNumber());
    }
    
    private BigDecimal visit(
        Predicate<Invoice> invoicePredicate,
        Function<InvoiceEntry, BigDecimal> invoiceEntryToValue
    ) throws SQLException {
        return database.getAll().stream()
            .filter(invoicePredicate)
            .flatMap(i -> i.getEntries().stream())
            .map(invoiceEntryToValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
