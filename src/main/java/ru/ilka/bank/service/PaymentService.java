package ru.ilka.bank.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.domain.db.AccountTransaction;
import ru.ilka.bank.domain.db.Currency;
import ru.ilka.bank.exception.RestException;
import ru.ilka.bank.repository.AccountTransactionRepository;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    CurrencyConversionService currencyConversionService;
    BalanceService balanceService;
    AccountTransactionRepository transactionRepository;

    @Transactional
    public AccountTransaction withdrawal(@NotNull Account account, @NotNull BigDecimal amount) {
        checkIfAccountHasEnoughBalance(account, amount);

        return transactionRepository.save(AccountTransaction.builder()
                .type(TransactionTypeEnum.DEBIT)
                .account(account)
                .amount(amount)
                .build());
    }

    @Transactional
    public AccountTransaction refillment(@NotNull Account account,
                                         @NotNull BigDecimal amount,
                                         @NotNull Currency currency) {
        if (!account.getCurrency().equals(currency)) {
            amount = currencyConversionService.convert(amount, currency, account.getCurrency());
        }
        return transactionRepository.save(AccountTransaction.builder()
                .type(TransactionTypeEnum.CREDIT)
                .account(account)
                .amount(amount)
                .build());
    }

    private void checkIfAccountHasEnoughBalance(Account accountFrom, BigDecimal amount) {
        if (balanceService.calculateAccountBalance(accountFrom).getAmount().compareTo(amount) < 0) {
            throw new RestException("Insufficient funds!", HttpStatus.BAD_REQUEST);
        }
    }
}
