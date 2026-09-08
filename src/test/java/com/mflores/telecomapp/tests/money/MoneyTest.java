package com.mflores.telecomapp.tests.money;

import com.mflores.telecomapp.model.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Currency;

public class MoneyTest {

    @Test
    void shouldThrowIllegalArgumentExceptionWhenAmountIsNegative() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Money money = new Money(-1L, Currency.getInstance("USD"));
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCurrencyIsNull() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Money money = new Money(10000000L, null);
        });
    }

    @Test
    void shouldCreateMoneyConstructorSuccessfully() {


        Money money = new Money(10000000L, Currency.getInstance("USD"));

        Assertions.assertEquals(10000000L, money.amountInCents());
        Assertions.assertEquals(Currency.getInstance("USD"), money.currency());
    }

    @Test
    void shouldCreateMoneyConstructorSuccessfullyWithAmountZero() {
        Money money = new Money(0, Currency.getInstance("USD"));

        Assertions.assertEquals(0, money.amountInCents());
    }

    @Test
    void shouldSumTwoAmountWhenTheyAreTheSameCurrency() {
        Money money = new Money(1000L, Currency.getInstance("USD"));
        Money money2 = new Money(500L, Currency.getInstance("USD"));
        Money result = money.add(money2);

        Assertions.assertEquals(1500, result.amountInCents());
    }

    @Test
    void shouldNotSumTwoAmountWhenTheyAreTheDifferentCurrency() {
        Money money = new Money(1000L, Currency.getInstance("USD"));
        Money money2 = new Money(500L, Currency.getInstance("MXN"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            money.add(money2);
        });

    }
}
