package ru.ilka.bank.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ilka.bank.domain.Balance;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.dto.AccountDto;
import ru.ilka.bank.mapper.AccountMapper;
import ru.ilka.bank.mapper.BalanceMapper;
import ru.ilka.bank.service.AccountService;
import ru.ilka.bank.service.BalanceService;

import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping(path = "/api/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    private static final String IBAN_REGEX = "[BY]{2}\\d{2}[A-Z]{4}\\d{20}";
    AccountService accountService;
    BalanceService balanceService;
    AccountMapper accountMapper;
    BalanceMapper balanceMapper;

    @GetMapping("/{iban}")
    @ApiOperation("Get account info with balance by iban.")
    public AccountDto getAccountByIban(@PathVariable("iban") @Pattern(regexp = IBAN_REGEX, message = "Invalid iban")
                                       @ApiParam(example = "BY20OLMP31350000001000000933") String iban,
                                       @RequestParam(value = "withBalance", required = false) boolean withBalance) {
        Account account = accountService.findByIban(iban);
        AccountDto accountDto = accountMapper.toDto(account);
        if (withBalance) {
            Balance balance = balanceService.calculateAccountBalance(account);
            accountDto.setBalanceDto(balanceMapper.toDto(balance));
        }
        return accountDto;
    }
}
