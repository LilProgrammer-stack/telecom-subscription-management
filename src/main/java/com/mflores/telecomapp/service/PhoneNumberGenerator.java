package com.mflores.telecomapp.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberGenerator {

    private final JdbcTemplate  jdbcTemplate;

    public PhoneNumberGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generatePhoneNumber() {
        Long sequenceValue = jdbcTemplate.queryForObject(
                "SELECT nextval('phone_number_seq')", Long.class
        );
        return "2125"+ String.format("%06d",sequenceValue);
    }
}
