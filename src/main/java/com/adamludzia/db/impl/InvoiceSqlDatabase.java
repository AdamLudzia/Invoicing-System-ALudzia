package com.adamludzia.db.impl;

import com.adamludzia.db.Database;
import com.adamludzia.model.Car;
import com.adamludzia.model.Company;
import com.adamludzia.model.Invoice;
import com.adamludzia.model.InvoiceEntry;
import com.adamludzia.model.Vat;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.flywaydb.core.internal.jdbc.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class InvoiceSqlDatabase extends AbstractSqlDatabase implements Database<Invoice> {

    public static final String SELECT_QUERY = "select i.id, i.date, i.number, "
        + "c1.id as seller_id, c1.name as seller_name, c1.tax_identification_number as seller_tax_id, c1.address as seller_address, "
        + "c1.pension_insurance as seller_pension_insurance, c1.health_insurance as seller_health_insurance, "
        + "c2.id as buyer_id, c2.name as buyer_name, c2.tax_identification_number as buyer_tax_id, c2.address as buyer_address, "
        + "c2.pension_insurance as buyer_pension_insurance, c2.health_insurance as buyer_health_insurance "
        + "from invoice i "
        + "inner join company c1 on i.seller = c1.id "
        + "inner join company c2 on i.buyer = c2.id";

    public InvoiceSqlDatabase(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    @Transactional
    public long save(Invoice invoice) {
        int buyerId = insertCompany(invoice.getBuyer());
        int sellerId = insertCompany(invoice.getSeller());

        int invoiceId = insertInvoice(invoice, buyerId, sellerId);
        addEntriesRelatedToInvoice(invoiceId, invoice);

        return invoiceId;
    }

    private int insertInvoice(Invoice invoice, int buyerId, int sellerId) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                connection.prepareStatement("insert into invoice (date, number, buyer, seller) values (?, ?, ?, ?);", new String[] {"id"});
            ps.setDate(1, Date.valueOf(invoice.getDate()));
            ps.setString(2, invoice.getNumber());
            ps.setLong(3, buyerId);
            ps.setLong(4, sellerId);
            return ps;
        }, keyHolder);

        int invoiceId = keyHolder.getKey().intValue();
        return invoiceId;
    }

    private Integer insertCarAndGetItId(Car car) {
        if (car == null) {
            return null;
        }

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                .prepareStatement(
                    "insert into car (registration_number, personal_use) values (?, ?);",
                    new String[] {"id"});
            ps.setString(1, car.getRegistrationPlate());
            ps.setBoolean(2, car.isPersonalUsage());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public List<Invoice> getAll() throws SQLException {
        return jdbcTemplate.query(SELECT_QUERY, invoiceRowMapper());
    }

    @Override
    public Optional<Invoice> getById(long id) throws SQLException {
        List<Invoice> invoices = jdbcTemplate.query(SELECT_QUERY + " where i.id = " + id, invoiceRowMapper());

        return invoices.isEmpty() ? Optional.empty() : Optional.of(invoices.get(0));
    }

    private RowMapper<Invoice> invoiceRowMapper() {
        return (rs, rowNr) -> {
            long invoiceId = rs.getLong("id");

            List<InvoiceEntry> invoiceEntries = jdbcTemplate.query(
                "select * from invoice_invoice_entry iie "
                    + "inner join invoice_entry e on iie.invoice_entry_id = e.id "
                    + "left outer join car c on e.expense_related_to_car = c.id "
                    + "where invoice_id = " + invoiceId,
                (response, ignored) -> InvoiceEntry.builder()
                    .description(response.getString("description"))
                    .quantity(response.getBigDecimal("quantity"))
                    .price(response.getBigDecimal("net_price"))
                    .vatValue(response.getBigDecimal("vat_value"))
                    .vatRate(Vat.valueOf(response.getString("vat_rate")))
                    .carExpenses(response.getObject("registration_number") != null
                        ? Car.builder()
                        .registrationPlate(response.getString("registration_number"))
                        .personalUsage(response.getBoolean("personal_use"))
                        .build()
                        : null)
                    .build());

            return Invoice.builder()
                .id(rs.getLong("id"))
                .date(rs.getDate("date").toLocalDate())
                .number(rs.getString("number"))
                .buyer(Company.builder()
                    .id(rs.getLong("buyer_id"))
                    .taxIdentificationNumber(rs.getString("buyer_tax_id"))
                    .name(rs.getString("buyer_name"))
                    .address(rs.getString("buyer_address"))
                    .pensionInsurance(rs.getBigDecimal("buyer_pension_insurance"))
                    .healthInsurance(rs.getBigDecimal("buyer_health_insurance"))
                    .build()
                )
                .seller(Company.builder()
                    .id(rs.getLong("seller_id"))
                    .taxIdentificationNumber(rs.getString("seller_tax_id"))
                    .name(rs.getString("seller_name"))
                    .address(rs.getString("seller_address"))
                    .pensionInsurance(rs.getBigDecimal("seller_pension_insurance"))
                    .healthInsurance(rs.getBigDecimal("seller_health_insurance"))
                    .build()
                )
                .entries(invoiceEntries)
                .build();
        };
    }

    @Override
    @Transactional
    public Optional<Invoice> update(long id, Invoice updatedInvoice) throws SQLException {
        Optional<Invoice> originalInvoice = getById(id);

        if (originalInvoice.isEmpty()) {
            return originalInvoice;
        }

        updatedInvoice.getBuyer().setId(originalInvoice.get().getBuyer().getId());
        updateCompany(updatedInvoice.getBuyer());

        updatedInvoice.getSeller().setId(originalInvoice.get().getSeller().getId());
        updateCompany(updatedInvoice.getSeller());

        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                connection.prepareStatement(
                    "update invoice "
                        + "set date=?, "
                        + "number=? "
                        + "where id=?"
                );
            ps.setDate(1, Date.valueOf(updatedInvoice.getDate()));
            ps.setString(2, updatedInvoice.getNumber());
            ps.setLong(3, id);
            return ps;
        });

        deleteEntriesAndCarsRelatedToInvoice(id);
        addEntriesRelatedToInvoice(id, updatedInvoice);

        return originalInvoice;
    }

    private void addEntriesRelatedToInvoice(long invoiceId, Invoice invoice) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        invoice.getEntries().forEach(entry -> {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                    .prepareStatement(
                        "insert into invoice_entry (description, quantity, net_price, vat_value, vat_rate, expense_related_to_car) "
                            + "values (?, ?, ?, ?, ?, ?);",
                        new String[] {"id"});
                ps.setString(1, entry.getDescription());
                ps.setBigDecimal(2, entry.getQuantity());
                ps.setBigDecimal(3, entry.getPrice());
                ps.setBigDecimal(4, entry.getVatValue());
                ps.setString(5, entry.getVatRate().name());
                ps.setObject(6, insertCarAndGetItId(entry.getCarExpenses()));
                return ps;
            }, keyHolder);

            int invoiceEntryId = keyHolder.getKey().intValue();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "insert into invoice_invoice_entry (invoice_id, invoice_entry_id) values (?, ?);");
                ps.setLong(1, invoiceId);
                ps.setLong(2, invoiceEntryId);
                return ps;
            });
        });
    }

    @Override
    @Transactional
    public Optional<Invoice> delete(long id) throws SQLException {
        Optional<Invoice> invoiceOptional = getById(id);
        if (invoiceOptional.isEmpty()) {
            return invoiceOptional;
        }

        Invoice invoice = invoiceOptional.get();

        deleteEntriesAndCarsRelatedToInvoice(id);

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "delete from invoice where id = ?;");
            ps.setLong(1, id);
            return ps;
        });

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "delete from company where id in (?, ?);");
            ps.setLong(1, invoice.getBuyer().getId());
            ps.setLong(2, invoice.getSeller().getId());
            return ps;
        });

        return invoiceOptional;
    }

    private void deleteEntriesAndCarsRelatedToInvoice(long id) {
        jdbcTemplate.update(connection -> { // warn: makes use of delete cascade
            PreparedStatement ps = connection.prepareStatement("delete from car where id in ("
                + "select expense_related_to_car from invoice_entry where id in ("
                + "select invoice_entry_id from invoice_invoice_entry where invoice_id=?));");
            ps.setLong(1, id);
            return ps;
        });

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "delete from invoice_entry where id in (select invoice_entry_id from invoice_invoice_entry where invoice_id=?);");
            ps.setLong(1, id);
            return ps;
        });
    }

}
