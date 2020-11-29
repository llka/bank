package ru.ilka.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ilka.bank.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}