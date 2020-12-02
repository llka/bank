package ru.ilka.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.domain.db.Currency;
import ru.ilka.bank.domain.db.Transaction;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.currency = :#{#currency} AND " +
            " t.account = :#{#account} AND t.type = :#{#type}")
    Optional<BigDecimal> calculateAmountSumByTransactionTypeAccountAndCurrency(
            @Param("type") TransactionTypeEnum type,
            @Param("account") Account account,
            @Param("currency") Currency currency);

}
