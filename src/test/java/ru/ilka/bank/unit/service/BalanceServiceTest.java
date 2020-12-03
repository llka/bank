package ru.ilka.bank.unit.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.repository.AccountTransactionRepository;
import ru.ilka.bank.service.BalanceService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.ilka.bank.unit.util.ModelGenerator.account;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BalanceServiceTest {
    private static final Account ACCOUNT = account();
    AccountTransactionRepository transactionRepository = mock(AccountTransactionRepository.class);
    BalanceService balanceService = new BalanceService(transactionRepository);

    @Test
    public void calculateAccountBalance_shouldCalculateDebitAndCreditTransactions() {
        when(transactionRepository.calculateAmountSumByTransactionTypeAccount(any(), any()))
                .thenReturn(Optional.of(BigDecimal.ONE));

        balanceService.calculateAccountBalance(account());

        verify(transactionRepository, times(1))
                .calculateAmountSumByTransactionTypeAccount(TransactionTypeEnum.DEBIT, ACCOUNT);
        verify(transactionRepository, times(1))
                .calculateAmountSumByTransactionTypeAccount(TransactionTypeEnum.CREDIT, ACCOUNT);
    }

    @Test
    public void calculateAccountBalance_whenNoTransactions_shouldReturnZero() {
        when(transactionRepository.calculateAmountSumByTransactionTypeAccount(any(), any()))
                .thenReturn(Optional.empty());

        var balance = balanceService.calculateAccountBalance(account());

        assertThat(balance, is(notNullValue()));
        assertThat(balance.getAmount(), comparesEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void calculateAccountBalance_shouldReturnBalanceInAccountCurrency() {
        when(transactionRepository.calculateAmountSumByTransactionTypeAccount(any(), any()))
                .thenReturn(Optional.empty());

        var balance = balanceService.calculateAccountBalance(account());

        assertThat(balance, is(notNullValue()));
        assertThat(balance.getCurrency(), is(ACCOUNT.getCurrency()));
    }

    @Test
    public void calculateAccountBalance_whenFoundTransactions_shouldReturnCreditMinusDebit() {
        BigDecimal debitSum = BigDecimal.ONE;
        BigDecimal creditSum = BigDecimal.TEN;
        when(transactionRepository.calculateAmountSumByTransactionTypeAccount(TransactionTypeEnum.CREDIT, ACCOUNT))
                .thenReturn(Optional.of(creditSum));
        when(transactionRepository.calculateAmountSumByTransactionTypeAccount(TransactionTypeEnum.DEBIT, ACCOUNT))
                .thenReturn(Optional.of(debitSum));

        var balance = balanceService.calculateAccountBalance(account());

        assertThat(balance, is(notNullValue()));
        assertThat(balance.getAmount(), comparesEqualTo(creditSum.subtract(debitSum)));
    }
}
