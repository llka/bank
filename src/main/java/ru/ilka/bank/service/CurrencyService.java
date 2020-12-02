package ru.ilka.bank.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.ilka.bank.domain.CurrencyCodeEnum;
import ru.ilka.bank.domain.db.Currency;
import ru.ilka.bank.exception.RestException;
import ru.ilka.bank.repository.CurrencyRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrencyService {
    CurrencyRepository currencyRepository;

    public Currency findByCurrencyCode(CurrencyCodeEnum currencyCode) {
        return currencyRepository.findByCode(currencyCode)
                .orElseThrow(() -> new RestException("Currency not found by code: " + currencyCode, HttpStatus.NOT_FOUND));
    }
}
