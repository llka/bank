package ru.ilka.bank.db;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.domain.db.Currency;
import ru.ilka.bank.domain.db.Transaction;
import ru.ilka.bank.repository.AccountRepository;
import ru.ilka.bank.repository.CurrencyRepository;
import ru.ilka.bank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

@Profile("local")
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DbSeeding {
    CurrencyRepository currencyRepository;
    AccountRepository accountRepository;
    TransactionRepository transactionRepository;


    public DbSeeding(CurrencyRepository currencyRepository,
                     AccountRepository accountRepository,
                     TransactionRepository transactionRepository) {
        this.currencyRepository = currencyRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;

        initData();
    }

    public void initData() {
        Currency currency = currencyRepository.save(Currency.builder()
                .code("USD")
                .fullName("Dollar")
                .build());

        Account account = accountRepository.save(Account.builder()
                .iban("BY20OLMP31350000001000000933")
                .currency(currency)
                .build());

        transactionRepository.saveAll(List.of(
                Transaction.builder()
                        .account(account)
                        .amount(BigDecimal.TEN)
                        .currency(currency)
                        .type(TransactionTypeEnum.CREDIT)
                        .build(),
                Transaction.builder()
                        .account(account)
                        .amount(BigDecimal.ONE)
                        .currency(currency)
                        .type(TransactionTypeEnum.DEBIT)
                        .build())
        );
    }
}
