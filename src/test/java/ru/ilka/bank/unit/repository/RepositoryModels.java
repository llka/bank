package ru.ilka.bank.unit.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.ilka.bank.domain.CurrencyCodeEnum;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.domain.db.Currency;
import ru.ilka.bank.domain.db.AccountTransaction;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepositoryModels {

    public static Currency CURRENCY = Currency.builder()
            .code(CurrencyCodeEnum.USD)
            .fullName("Dollar")
            .build();

    public static Account ACCOUNT = Account.builder()
            .iban("BY20OLMP31350000001000000933")
            .currency(CURRENCY)
            .build();


    public static AccountTransaction ACCOUNT_TRANSACTION_DEBIT = AccountTransaction.builder()
            .account(ACCOUNT)
            .amount(BigDecimal.ONE)
            .type(TransactionTypeEnum.DEBIT)
            .build();

    public static AccountTransaction ACCOUNT_TRANSACTION_CREDIT = AccountTransaction.builder()
            .account(ACCOUNT)
            .amount(BigDecimal.TEN)
            .type(TransactionTypeEnum.CREDIT)
            .build();

    public static AccountTransaction ACCOUNT_TRANSACTION_DEBIT_2 = AccountTransaction.builder()
            .account(ACCOUNT)
            .amount(BigDecimal.ONE)
            .type(TransactionTypeEnum.DEBIT)
            .build();

    public static AccountTransaction ACCOUNT_TRANSACTION_CREDIT_2 = AccountTransaction.builder()
            .account(ACCOUNT)
            .amount(BigDecimal.TEN)
            .type(TransactionTypeEnum.CREDIT)
            .build();
}
