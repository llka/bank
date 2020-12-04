package ru.ilka.bank.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.ilka.bank.domain.TransactionTypeEnum;
import ru.ilka.bank.domain.db.AccountTransaction;
import ru.ilka.bank.repository.AccountRepository;
import ru.ilka.bank.repository.AccountTransactionRepository;
import ru.ilka.bank.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.ilka.bank.unit.repository.RepositoryModels.ACCOUNT;
import static ru.ilka.bank.unit.repository.RepositoryModels.CURRENCY;
import static ru.ilka.bank.unit.util.ModelGenerator.IBAN;

@ActiveProfiles(profiles = "test")
@SpringBootTest
@AutoConfigureMockMvc
public class RaceConditionTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private AccountTransactionRepository transactionRepository;

    @Test
    public void replenish2() throws Exception {

        CURRENCY = currencyRepository.save(CURRENCY);
        ACCOUNT = accountRepository.save(ACCOUNT);
        transactionRepository.save(AccountTransaction.builder()
                .account(ACCOUNT)
                .amount(BigDecimal.valueOf(100))
                .type(TransactionTypeEnum.CREDIT)
                .build());

        mockMvc.perform(get("/api/accounts/{iban}?withBalance=true", IBAN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban", is(IBAN)))
                .andExpect(jsonPath("balance", comparesEqualTo(100.0)));

        Random random = new Random();
        List<Thread> tlist = new ArrayList<>(100);
        for (int i = 0; i < 40; i++) {
            final  int x = i;
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(random.nextInt(100) + 1);

                    MockHttpServletResponse response = mockMvc.perform(post("/api/accounts/{iban}/withdrawal", IBAN)
                            .contentType(APPLICATION_JSON)
                            .content("{\n" +
                                    "    \"amount\": 3\n" +
                                    "  }")
                            .accept(APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andReturn().getResponse();

                    System.out.println("Result: " + x +" : " + "result: " + response.getStatus());
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            });
            t.start();
//            t.join();
            tlist.add(t);
        }
        for (Thread thread : tlist) {
            thread.join();
        }

        mockMvc.perform(get("/api/accounts/{iban}?withBalance=true", IBAN)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban", is(IBAN)))
                .andExpect(jsonPath("balance", comparesEqualTo(1.0)));
    }

}
