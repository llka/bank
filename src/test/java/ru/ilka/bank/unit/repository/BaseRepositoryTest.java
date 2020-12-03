package ru.ilka.bank.unit.repository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.ilka.bank.repository.AccountRepository;
import ru.ilka.bank.repository.AccountTransactionRepository;
import ru.ilka.bank.repository.CurrencyRepository;

import static ru.ilka.bank.unit.repository.RepositoryModels.ACCOUNT;
import static ru.ilka.bank.unit.repository.RepositoryModels.ACCOUNT_TRANSACTION_CREDIT;
import static ru.ilka.bank.unit.repository.RepositoryModels.ACCOUNT_TRANSACTION_CREDIT_2;
import static ru.ilka.bank.unit.repository.RepositoryModels.ACCOUNT_TRANSACTION_DEBIT;
import static ru.ilka.bank.unit.repository.RepositoryModels.ACCOUNT_TRANSACTION_DEBIT_2;
import static ru.ilka.bank.unit.repository.RepositoryModels.CURRENCY;

@DataJpaTest
@Import(JpaTestConfig.class)
@FieldDefaults(level = AccessLevel.PROTECTED)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseRepositoryTest {
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountTransactionRepository transactionRepository;

    @BeforeAll
    public void initDatabase() {
        CURRENCY = currencyRepository.save(CURRENCY);
        ACCOUNT = accountRepository.save(ACCOUNT);
        ACCOUNT_TRANSACTION_CREDIT = transactionRepository.save(ACCOUNT_TRANSACTION_CREDIT);
        ACCOUNT_TRANSACTION_DEBIT = transactionRepository.save(ACCOUNT_TRANSACTION_DEBIT);
        ACCOUNT_TRANSACTION_CREDIT_2 = transactionRepository.save(ACCOUNT_TRANSACTION_CREDIT_2);
        ACCOUNT_TRANSACTION_DEBIT_2 = transactionRepository.save(ACCOUNT_TRANSACTION_DEBIT_2);
    }
}
