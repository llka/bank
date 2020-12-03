package ru.ilka.bank.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.ilka.bank.domain.db.Currency;
import ru.ilka.bank.exception.FeatureNotImplementedException;

import java.math.BigDecimal;

@Service
public class CurrencyConversionService {
    public BigDecimal convert(@NonNull BigDecimal amount, @NonNull Currency from, @NonNull Currency to) {
        if (from.equals(to) || BigDecimal.ZERO.equals(amount)) {
            return amount;
        }
        throw new FeatureNotImplementedException("Currency conversion");
    }
}
