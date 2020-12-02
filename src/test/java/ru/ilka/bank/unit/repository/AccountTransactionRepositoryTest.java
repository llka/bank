package ru.ilka.bank.unit.repository;

import org.junit.jupiter.api.Test;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.Account;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
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
}
