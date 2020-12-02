package ru.ilka.bank.unit.repository;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.ilka.bank.unit.repository.RepositoryModels.ACCOUNT;

public class AccountRepositoryTest extends BaseRepositoryTest {

    @Test
    public void findByIbanTest() {
        var account = accountRepository.findByIban(ACCOUNT.getIban());

        assertThat(account.isPresent(), is(true));
    }

    @Test
    public void findByIbanTest_whenNotFound() {
        var account = accountRepository.findByIban("noSuchIban");

        assertThat(account.isEmpty(), is(true));
    }
}
