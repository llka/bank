package ru.ilka.bank.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.exception.RestException;
import ru.ilka.bank.repository.AccountRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
    AccountRepository accountRepository;

    public Account findByIban(String iban) {
        return accountRepository.findByIban(iban)
                .orElseThrow(() -> new RestException("Cannot find account with iban: " + iban, HttpStatus.NOT_FOUND));
    }
}
