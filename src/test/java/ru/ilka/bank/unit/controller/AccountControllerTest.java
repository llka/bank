package ru.ilka.bank.unit.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.ilka.bank.controller.AccountController;
import ru.ilka.bank.service.AccountService;
import ru.ilka.bank.service.BalanceService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.ilka.bank.unit.controller.ControllerModels.IBAN;
import static ru.ilka.bank.unit.controller.ControllerModels.account;
import static ru.ilka.bank.unit.controller.ControllerModels.balance;

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

    @Test
    public void getAccountByIbanTest_withoutBalance() throws Exception {
        when(accountService.findByIban(IBAN)).thenReturn(account());

        mockMvc.perform(get(ACCOUNTS_PATH + GET_ACCOUNT_BY_IBAN_PATH, IBAN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban", is(IBAN)))
                .andExpect(jsonPath("$.currencyCode", is(account().getCurrency().getCode())));
    }

    @Test
    public void getAccountByIbanTest_withBalance() throws Exception {
        when(accountService.findByIban(ControllerModels.IBAN)).thenReturn(account());
        when(balanceService.calculateAccountBalance(account())).thenReturn(balance());

        mockMvc.perform(get(ACCOUNTS_PATH + GET_ACCOUNT_BY_IBAN_PATH, IBAN)
                .param(WITH_BALANCE_PARAM_NAME, "true")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban", is(IBAN)))
                .andExpect(jsonPath("$.currencyCode", is(account().getCurrency().getCode())))
                .andExpect(jsonPath("$.balance.amount", is(balance().getAmount().doubleValue())))
                .andExpect(jsonPath("$.balance.currencyCode", is(balance().getCurrency().getCode())));
    }
}
