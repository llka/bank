package ru.ilka.bank.unit.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.ilka.bank.domain.db.Account;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static ru.ilka.bank.unit.repository.RepositoryModels.ACCOUNT;
import static ru.ilka.bank.unit.repository.RepositoryModels.CURRENCY;

class AccountRepositoryTest extends BaseRepositoryTest {

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


    @Nested
    class AuditingTest {

        @Test
        public void save_shouldSetCreatedOn() {
            Account account = account();

            account = accountRepository.save(account);

            assertThat(account, is(notNullValue()));
            assertThat(account.getCreatedOn(), is(notNullValue()));
            accountRepository.delete(account);
        }

        @Disabled
        @Test
        public void saveOnUpdate_shouldUpdateLastModifiedOn() {
            ACCOUNT.setIban("new");

            ACCOUNT = accountRepository.save(ACCOUNT);

            assertThat(ACCOUNT, is(notNullValue()));
            assertThat(ACCOUNT.getCreatedOn(), is(notNullValue()));
            assertThat(ACCOUNT.getLastModifiedOn(), is(notNullValue()));
            assertThat("account lastModifiedOn should be changed",
                    ACCOUNT.getLastModifiedOn().isAfter(ACCOUNT.getCreatedOn()), is(true));
        }

        private Account account() {
            return Account.builder()
                    .iban("test")
                    .currency(CURRENCY)
                    .build();
        }
    }
}
