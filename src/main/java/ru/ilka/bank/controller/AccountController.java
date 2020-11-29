package ru.ilka.bank.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ilka.bank.dto.AccountDto;
import ru.ilka.bank.mapper.AccountMapper;
import ru.ilka.bank.service.AccountService;

import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping(path = "/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private static final String IBAN_REGEX = "[BY]{2}\\d{2}[A-Z]{4}\\d{20}";
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping("/{iban}")
    @ApiOperation("Get account info with balance by iban.")
    public AccountDto getAccountByIban(@PathVariable("iban") @Pattern(regexp = IBAN_REGEX, message = "Invalid iban")
                                       @ApiParam(example = "BY20OLMP31350000001000000933") String iban) {
        return accountMapper.toDto(accountService.findByIban(iban));
    }
}
