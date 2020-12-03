package ru.ilka.bank.unit.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.ilka.bank.domain.CurrencyCodeEnum;
import ru.ilka.bank.domain.db.Currency;
import ru.ilka.bank.exception.RestException;
import ru.ilka.bank.repository.CurrencyRepository;
import ru.ilka.bank.service.CurrencyService;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.ilka.bank.unit.util.ModelGenerator.currency;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrencyServiceTest {
    CurrencyRepository currencyRepository = mock(CurrencyRepository.class);
    CurrencyService currencyService = new CurrencyService(currencyRepository);

    @Test
    public void findByCurrencyCode_whenFound_shouldReturnCurrency() {
        Currency currency = currency();
        when(currencyRepository.findByCode(CurrencyCodeEnum.USD)).thenReturn(Optional.of(currency));

        var result = currencyService.findByCurrencyCode(CurrencyCodeEnum.USD);

        assertThat(result, is(currency));
    }

    @Test
    public void findByCurrencyCode_whenNotFound_shouldThrowException() {
        when(currencyRepository.findByCode(CurrencyCodeEnum.USD)).thenReturn(Optional.empty());

        RestException exception = assertThrows(RestException.class,
                () -> currencyService.findByCurrencyCode(CurrencyCodeEnum.USD));

        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }
}
