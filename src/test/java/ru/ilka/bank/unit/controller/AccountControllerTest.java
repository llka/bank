package ru.ilka.bank.unit.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import ru.ilka.bank.controller.AccountController;
import ru.ilka.bank.service.AccountService;
import ru.ilka.bank.service.BalanceService;
import ru.ilka.bank.service.CurrencyService;
import ru.ilka.bank.service.PaymentService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.ilka.bank.unit.util.ModelGenerator.IBAN;
import static ru.ilka.bank.unit.util.ModelGenerator.account;
import static ru.ilka.bank.unit.util.ModelGenerator.balance;

@WebMvcTest(AccountController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountControllerTest extends BaseControllerTest {
    private static final String ACCOUNTS_PATH = "/api/accounts";
    private static final String GET_ACCOUNT_BY_IBAN_PATH = "/{iban}";
    private static final String WITH_BALANCE_PARAM_NAME = "withBalance";

    @MockBean
    AccountService accountService;
    @MockBean
    BalanceService balanceService;
    @MockBean
    PaymentService paymentService;
    @MockBean
    CurrencyService currencyService;

    @Test
    public void getAccountByIbanTest_withoutBalance() throws Exception {
        when(accountService.findByIban(IBAN)).thenReturn(account());

        mockMvc.perform(get(ACCOUNTS_PATH + GET_ACCOUNT_BY_IBAN_PATH, IBAN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban", is(IBAN)))
                .andExpect(jsonPath("$.currencyCode", is(account().getCurrency().getCode().name())));
    }

    @Test
    public void getAccountByIbanTest_withBalance() throws Exception {
        when(accountService.findByIban(IBAN)).thenReturn(account());
        when(balanceService.calculateAccountBalance(account())).thenReturn(balance());

        mockMvc.perform(get(ACCOUNTS_PATH + GET_ACCOUNT_BY_IBAN_PATH, IBAN)
                .param(WITH_BALANCE_PARAM_NAME, "true")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban", is(IBAN)))
                .andExpect(jsonPath("$.currencyCode", is(account().getCurrency().getCode().name())))
                .andExpect(jsonPath("$.balance", is(balance().getAmount().doubleValue())));
    }
}
