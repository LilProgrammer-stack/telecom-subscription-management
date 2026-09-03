package com.mflores.telecomapp.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {

    private final JdbcTemplate jdbcTemplate;

    public AccountNumberGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generate(){
        Long sequenceValue = jdbcTemplate.queryForObject(
                "SELECT nextval('account_number_seq')", Long.class
        );
        String generatedSequenceValue;

        return generatedSequenceValue = "TB"+sequenceValue;
    }
}
