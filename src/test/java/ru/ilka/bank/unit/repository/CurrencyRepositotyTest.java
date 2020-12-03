package ru.ilka.bank.unit.repository;

import org.junit.jupiter.api.Test;
import ru.ilka.bank.domain.CurrencyCodeEnum;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CurrencyRepositotyTest extends BaseRepositoryTest {
    @Test
    public void findByCodeTest() {
        var result = currencyRepository.findByCode(CurrencyCodeEnum.USD);

        assertThat(result.isPresent(), is(true));
    }

    @Test
    public void findByCodeTest_whenNotFound_shouldReturnEmptyOptional() {
        var result = currencyRepository.findByCode(CurrencyCodeEnum.EURO);

        assertThat(result.isEmpty(), is(true));
    }
}
