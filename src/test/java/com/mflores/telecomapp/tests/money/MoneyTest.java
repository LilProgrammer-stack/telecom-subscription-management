package com.mflores.telecomapp.tests.money;

import com.mflores.telecomapp.model.Money;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Currency;

public class MoneyTest {

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCurrencyIsNull() {

        assertThrows(IllegalArgumentException.class, () -> {
            Money money = new Money(10000000L, null);
        });
    }

    @Test
    void shouldCreateMoneyConstructorSuccessfully() {


        Money money = new Money(10000000L, Currency.getInstance("USD"));

        assertEquals(10000000L, money.amountInCents());
        assertEquals(Currency.getInstance("USD"), money.currency());
    }

    @Test
    void shouldCreateMoneyConstructorSuccessfullyWithAmountZero() {
        Money money = new Money(0, Currency.getInstance("USD"));

        assertEquals(0, money.amountInCents());
    }

    @Test
    void shouldSumTwoAmountsWhenTheyAreTheSameCurrency() {
        Money money = new Money(1000L, Currency.getInstance("USD"));
        Money money2 = new Money(500L, Currency.getInstance("USD"));
        Money result = money.add(money2);

        assertEquals(1500L, result.amountInCents());
    }

    @Test
    void shouldNotSumTwoAmountsWhenTheyAreOfDifferentCurrency() {
        Money money = new Money(1000L, Currency.getInstance("USD"));
        Money money2 = new Money(500L, Currency.getInstance("MXN"));

        assertThrows(IllegalArgumentException.class, () -> {
            money.add(money2);
        });
    }

    @Test
    void shouldSubtractMoneyWhenTheyAreTheSameCurrency() {
        Money money = new Money(1000L, Currency.getInstance("USD"));
        Money money2 = new Money(-500L, Currency.getInstance("USD"));

        Money result = money.add(money2);

        assertEquals(500L, result.amountInCents());
    }

    @Test
    void shouldThrowArithmeticExceptionWhenAdditionOverflows() {
        Money money = new Money(Long.MAX_VALUE, Currency.getInstance("USD"));
        Money money2 = new Money(1L, Currency.getInstance("USD"));

        assertThrows(ArithmeticException.class, () -> {
            money.add(money2);
        });
    }

    @Test
    void shouldThrowExceptionWhenAddingNullMoney() {
        Money money = new Money(1000L, Currency.getInstance("USD"));

        assertThrows(IllegalArgumentException.class, () -> {
            money.add(null);
        });
    }
}
