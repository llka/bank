package ru.ilka.bank.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.ilka.bank.domain.Balance;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.repository.AccountTransactionRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BalanceService {
    AccountTransactionRepository transactionRepository;

    public Balance calculateAccountBalance(Account account) {
        BigDecimal amountPlus = transactionRepository
                .calculateAmountSumByTransactionTypeAccount(TransactionTypeEnum.CREDIT, account)
                .orElse(BigDecimal.ZERO);
        BigDecimal amountMinus = transactionRepository
                .calculateAmountSumByTransactionTypeAccount(TransactionTypeEnum.DEBIT, account)
                .orElse(BigDecimal.ZERO);

        return Balance.builder()
                .amount(amountPlus.subtract(amountMinus))
                .currency(account.getCurrency())
                .build();
    }
}
