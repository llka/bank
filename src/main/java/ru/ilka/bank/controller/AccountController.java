package ru.ilka.bank.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ilka.bank.domain.Balance;
import ru.ilka.bank.domain.db.Account;
import ru.ilka.bank.domain.db.Currency;
import ru.ilka.bank.dto.AccountDto;
import ru.ilka.bank.dto.AccountRefillmentRequestDto;
import ru.ilka.bank.dto.WithdrawalRequestDto;
import ru.ilka.bank.mapper.AccountMapper;
import ru.ilka.bank.service.AccountService;
import ru.ilka.bank.service.BalanceService;
import ru.ilka.bank.service.CurrencyService;
import ru.ilka.bank.service.PaymentService;

import javax.validation.Valid;
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
    PaymentService paymentService;
    CurrencyService currencyService;
    AccountMapper accountMapper;

    @GetMapping("/{iban}")
    @ApiOperation("Get account info with balance by iban.")
    @ApiImplicitParams({@ApiImplicitParam(name = "iban", value = "Account IBAN",
            required = true, dataType = "string", paramType = "path", example = "BY20OLMP31350000001000000933")})
    public AccountDto getAccountByIban(@PathVariable("iban")
                                       @Pattern(regexp = IBAN_REGEX, message = "Invalid iban") String iban,
                                       @RequestParam(value = "withBalance", required = false) boolean withBalance) {
        Account account = accountService.findByIban(iban);
        AccountDto accountDto = accountMapper.toDto(account);
        if (withBalance) {
            Balance balance = balanceService.calculateAccountBalance(account);
            accountDto.setBalance(balance.getAmount());
        }
        return accountDto;
    }

    @PostMapping("/{iban}/withdrawal")
    @ApiOperation("Withdraw amount from account balance to recipient account.")
    @ApiImplicitParams({@ApiImplicitParam(name = "iban", value = "Account IBAN",
            required = true, dataType = "string", paramType = "path", example = "BY20OLMP31350000001000000933")})
    public ResponseEntity<Void> withdrawAccountBalance(@PathVariable("iban")
                                                       @Pattern(regexp = IBAN_REGEX, message = "Invalid iban") String iban,
                                                       @RequestBody @Valid WithdrawalRequestDto withdrawalRequestDto) {

        Account account = accountService.findByIban(iban);
        paymentService.withdrawal(account, withdrawalRequestDto.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{iban}/refillment")
    @ApiOperation("Refill account balance.")
    @ApiImplicitParams({@ApiImplicitParam(name = "iban", value = "Account IBAN",
            required = true, dataType = "string", paramType = "path", example = "BY20OLMP31350000001000000933")})
    public ResponseEntity<Void> refillAccountBalance(@PathVariable("iban")
                                                     @Pattern(regexp = IBAN_REGEX, message = "Invalid iban") String iban,
                                                     @RequestBody @Valid AccountRefillmentRequestDto refillmentRequestDto) {
        Account account = accountService.findByIban(iban);
        Currency currency = currencyService.findByCurrencyCode(refillmentRequestDto.getCurrencyCode());
        paymentService.refillment(account, refillmentRequestDto.getAmount(), currency);
        return ResponseEntity.ok().build();
    }
}
