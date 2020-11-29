package ru.ilka.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.ilka.bank.entity.Account;
import ru.ilka.bank.exception.RestException;
import ru.ilka.bank.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account findByIban(String iban) {
        return accountRepository.findByIban(iban)
                .orElseThrow(() -> new RestException("Cannot find account with iban: " + iban, HttpStatus.NOT_FOUND));
    }
}
