package ru.ilka.bank.unit.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.domain.db.AccountTransaction;
import ru.ilka.bank.domain.db.Currency;
import ru.ilka.bank.exception.RestException;
import ru.ilka.bank.repository.AccountTransactionRepository;
import ru.ilka.bank.service.BalanceService;
import ru.ilka.bank.service.CurrencyConversionService;
import ru.ilka.bank.service.PaymentService;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.ilka.bank.unit.util.ModelGenerator.account;
import static ru.ilka.bank.unit.util.ModelGenerator.balance;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceTest {
    private static final Account ACCOUNT = account();

    CurrencyConversionService currencyConversionService = mock(CurrencyConversionService.class);
    BalanceService balanceService = mock(BalanceService.class);
    AccountTransactionRepository transactionRepository = mock(AccountTransactionRepository.class);

    PaymentService paymentService = new PaymentService(currencyConversionService,
            balanceService, transactionRepository);

    @BeforeEach
    public void setUpMocks() {
        when(transactionRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
    }

    @Test
    public void withdrawal_whenNotEnoughBalance_shouldReturnBadRequest() {
        when(balanceService.calculateAccountBalance(ACCOUNT)).thenReturn(balance(BigDecimal.ZERO));

        RestException exception = assertThrows(RestException.class, () -> paymentService.withdrawal(ACCOUNT, BigDecimal.TEN));

        assertThat(exception.getHttpStatus(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void withdrawal_whenEnoughBalance_shouldSaveDebitTransaction() {
        BigDecimal withdrawalAmount = BigDecimal.TEN;
        ArgumentCaptor<AccountTransaction> transactionCaptor = ArgumentCaptor.forClass(AccountTransaction.class);
        when(balanceService.calculateAccountBalance(ACCOUNT)).thenReturn(balance(BigDecimal.valueOf(100)));

        var transaction = paymentService.withdrawal(ACCOUNT, withdrawalAmount);

        verify(transactionRepository, times(1)).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getType(), is(TransactionTypeEnum.DEBIT));
        assertThat(transaction.getType(), is(TransactionTypeEnum.DEBIT));
    }

    @Test
    public void withdrawal_whenEnoughBalance_shouldSaveCorrectTransactionAmountAndAccount() {
        BigDecimal withdrawalAmount = BigDecimal.TEN;
        when(balanceService.calculateAccountBalance(ACCOUNT)).thenReturn(balance(BigDecimal.valueOf(100)));

        var transaction = paymentService.withdrawal(ACCOUNT, withdrawalAmount);

        verify(transactionRepository, times(1)).save(any());
        assertThat(transaction.getAmount(), comparesEqualTo(withdrawalAmount));
        assertThat(transaction.getAccount(), is(ACCOUNT));
    }

    @Test
    public void refillment_shouldSaveCreditTransaction() {
        BigDecimal amount = BigDecimal.TEN;
        Currency currency = ACCOUNT.getCurrency();

        ArgumentCaptor<AccountTransaction> transactionCaptor = ArgumentCaptor.forClass(AccountTransaction.class);

        var transaction = paymentService.refillment(ACCOUNT, amount, currency);

        verify(transactionRepository, times(1)).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getType(), is(TransactionTypeEnum.CREDIT));
        assertThat(transaction.getType(), is(TransactionTypeEnum.CREDIT));
    }

    @Test
    public void refillment_shouldSaveCorrectTransactionAmountAndAccount() {
        BigDecimal amount = BigDecimal.TEN;
        Currency currency = ACCOUNT.getCurrency();

        var transaction = paymentService.refillment(ACCOUNT, amount, currency);

        verify(transactionRepository, times(1)).save(any());
        assertThat(transaction.getAmount(), comparesEqualTo(amount));
        assertThat(transaction.getAccount(), is(ACCOUNT));
    }

    @Test
    public void refillment_whenRefillmentCurrencyNotEqualsAccountCurrency_shouldConvertAmountToAccountCurrency() {
        BigDecimal amount = BigDecimal.TEN;
        Currency currency = Currency.builder().build();

        paymentService.refillment(ACCOUNT, amount, currency);

        verify(currencyConversionService, times(1))
                .convert(amount, currency, ACCOUNT.getCurrency());
    }
}
