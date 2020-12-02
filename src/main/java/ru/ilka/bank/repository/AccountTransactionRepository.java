package ru.ilka.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.domain.db.AccountTransaction;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

    @Query("SELECT SUM(t.amount) FROM AccountTransaction t WHERE " +
            " t.account = :#{#account} AND t.type = :#{#type}")
    Optional<BigDecimal> calculateAmountSumByTransactionTypeAccount(
            @Param("type") TransactionTypeEnum type,
            @Param("account") Account account);

}
