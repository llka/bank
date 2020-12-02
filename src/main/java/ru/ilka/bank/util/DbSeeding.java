package ru.ilka.bank.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.ilka.bank.domain.CurrencyCodeEnum;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.domain.db.Currency;
import ru.ilka.bank.domain.db.AccountTransaction;
import ru.ilka.bank.repository.AccountRepository;
import ru.ilka.bank.repository.CurrencyRepository;
import ru.ilka.bank.repository.AccountTransactionRepository;

import java.math.BigDecimal;
import java.util.List;

@Profile("local")
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DbSeeding {
    CurrencyRepository currencyRepository;
    AccountRepository accountRepository;
    AccountTransactionRepository transactionRepository;


    public DbSeeding(CurrencyRepository currencyRepository,
                     AccountRepository accountRepository,
                     AccountTransactionRepository transactionRepository) {
        this.currencyRepository = currencyRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;

        initData();
    }

    public void initData() {
        Currency currency = currencyRepository.save(Currency.builder()
                .code(CurrencyCodeEnum.USD)
                .fullName("Dollar")
                .build());

        Account account = accountRepository.save(Account.builder()
                .iban("BY20OLMP31350000001000000933")
                .currency(currency)
                .build());

        transactionRepository.saveAll(List.of(
                AccountTransaction.builder()
                        .account(account)
                        .amount(BigDecimal.TEN)
                        .type(TransactionTypeEnum.CREDIT)
                        .build(),
                AccountTransaction.builder()
                        .account(account)
                        .amount(BigDecimal.ONE)
                        .type(TransactionTypeEnum.DEBIT)
                        .build())
        );
    }
}
