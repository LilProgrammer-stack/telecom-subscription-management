package com.mflores.telecomapp.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class InvoiceNumberGenerator {

    private final JdbcTemplate jdbcTemplate;

    public InvoiceNumberGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generateInvoiceNumber() {
        Long sequenceValue = jdbcTemplate.queryForObject(
                "SELECT nextval('account_number_seq')", Long.class
        );

        String generatedSequenceValue;

        return generatedSequenceValue = "INV"+sequenceValue;
    }
}
