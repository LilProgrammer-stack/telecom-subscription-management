package com.mflores.telecomapp.model;

import jakarta.persistence.Column;

import java.util.Currency;

public record Money(
        @Column(name = "total_amount_cents", nullable = false)
        long amountInCents,
        @Column(name = "currency", nullable = false, length = 3)
        Currency currency) {

    public Money {

        if (amountInCents < 0) {
            throw new IllegalArgumentException("amountInCents cannot be negative");
        }
        if (currency == null) {
            throw new IllegalArgumentException("currency cannot be null");
        }
    }

    public Money add(Money other) {

        if (this.currency.equals(other.currency)) {
            return new Money(this.amountInCents + other.amountInCents, currency);
        }else  {
            throw new IllegalArgumentException("Both amount's currency are not the same");
        }

    }
}
