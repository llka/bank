package ru.ilka.bank.unit.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.exception.RestException;
import ru.ilka.bank.repository.AccountRepository;
import ru.ilka.bank.service.AccountService;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.ilka.bank.unit.util.ModelGenerator.IBAN;
import static ru.ilka.bank.unit.util.ModelGenerator.account;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceTest {
    AccountRepository accountRepository = mock(AccountRepository.class);
    AccountService accountService = new AccountService(accountRepository);

    @Test
    public void findByIban_whenFound_shouldReturnAccount() {
        Account account = account();
        when(accountRepository.findByIban(IBAN)).thenReturn(Optional.of(account));

        var result = accountService.findByIban(IBAN);

        assertThat(result, is(account));
    }

    @Test
    public void findByIban_whenNotFound_shouldThrowException() {
        when(accountRepository.findByIban(IBAN)).thenReturn(Optional.empty());

        RestException exception = assertThrows(RestException.class, () -> accountService.findByIban(IBAN));

        assertThat(exception.getHttpStatus(), is(HttpStatus.NOT_FOUND));
    }

}
