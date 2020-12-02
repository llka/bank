package ru.ilka.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ilka.bank.domain.CurrencyCodeEnum;
import ru.ilka.bank.domain.db.Currency;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCode(CurrencyCodeEnum currencyCode);
}
