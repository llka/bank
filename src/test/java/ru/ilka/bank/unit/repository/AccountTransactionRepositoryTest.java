package ru.ilka.bank.unit.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.domain.db.AccountTransaction;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static ru.ilka.bank.unit.repository.RepositoryModels.ACCOUNT;
import static ru.ilka.bank.unit.repository.RepositoryModels.ACCOUNT_TRANSACTION_CREDIT;
import static ru.ilka.bank.unit.repository.RepositoryModels.ACCOUNT_TRANSACTION_CREDIT_2;

public class AccountTransactionRepositoryTest extends BaseRepositoryTest {
    @Test
    public void calculateAmountSumByTransactionTypeAccountAndCurrencyTest() {
        var result = transactionRepository
                .calculateAmountSumByTransactionTypeAccount(TransactionTypeEnum.CREDIT, ACCOUNT);

        assertThat(result.orElseThrow(),
                comparesEqualTo(ACCOUNT_TRANSACTION_CREDIT.getAmount().add(ACCOUNT_TRANSACTION_CREDIT_2.getAmount())));
    }

    @Test
    public void calculateAmountSumByTransactionTypeAccountAndCurrency_whenTransactionsNotFound_shouldReturnEmptyOpt() {
        Account account = Account.builder()
                .id(9999L)
                .iban("test")
                .build();

        var result = transactionRepository
                .calculateAmountSumByTransactionTypeAccount(TransactionTypeEnum.CREDIT, account);

        assertThat(result.isEmpty(), is(true));
    }

    @Nested
    class AuditingTest {
        AccountTransaction entity;

        @AfterEach
        void rollbackChanges() {
            transactionRepository.delete(entity);
            entity = null;
        }

        @Test
        void save_shouldSetCreatedOn() {
            entity = accountTransaction();

            entity = transactionRepository.save(entity);

            assertThat(entity, is(notNullValue()));
            assertThat(entity.getCreatedOn(), is(notNullValue()));
        }

        @Disabled
        @Test
        void saveOnUpdate_shouldUpdateLastModifiedOn() {
            entity = transactionRepository.save(accountTransaction());
            entity.setAmount(BigDecimal.ZERO);

            entity = transactionRepository.save(entity);

            assertThat(entity, is(notNullValue()));
            assertThat(entity.getCreatedOn(), is(notNullValue()));
            assertThat(entity.getLastModifiedOn(), is(notNullValue()));
            assertThat("account transaction lastModifiedOn should be changed",
                    entity.getLastModifiedOn().isAfter(entity.getCreatedOn()), is(true));
        }

        private AccountTransaction accountTransaction() {
            return AccountTransaction.builder()
                    .account(ACCOUNT)
                    .amount(BigDecimal.valueOf(100))
                    .type(TransactionTypeEnum.DEBIT)
                    .build();
        }
    }
}
