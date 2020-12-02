package ru.ilka.bank.unit.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.ilka.bank.domain.Balance;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.domain.db.Currency;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerModels {
    public static final String IBAN = "BY20OLMP31350000001000000933";

    public static Currency currency() {
        return Currency.builder()
                .id(1L)
                .code("USD")
                .fullName("Dollar")
                .build();
    }

    public static Account account() {
        return Account.builder()
                .id(1L)
                .iban(IBAN)
                .currency(currency())
                .build();
    }

    public static Balance balance() {
        return Balance.builder()
                .currency(currency())
                .amount(BigDecimal.valueOf(12.34))
                .build();
    }
}
