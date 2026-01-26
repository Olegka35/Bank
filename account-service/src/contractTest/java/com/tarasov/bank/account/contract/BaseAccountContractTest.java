package com.tarasov.bank.account.contract;

import com.tarasov.bank.account.model.dto.*;
import com.tarasov.bank.account.service.AccountService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;


@WebMvcTest(properties = { "spring.cloud.config.enabled=false" })
@AutoConfigureMockMvc
@ActiveProfiles("contract-test")
@Import(FilterChainConfiguration.class)
public class BaseAccountContractTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected AccountService accountService;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        when(accountService.existsByLogin("oleg")).thenReturn(true);
        when(accountService.updateAccountBalance("oleg", Action.PUT, BigDecimal.valueOf(500)))
                .thenReturn(new BalanceResponse(BigDecimal.valueOf(1000)));
        when(accountService.transferMoney("oleg", "polukhina", BigDecimal.valueOf(12000)))
                .thenReturn(new BalanceResponse(BigDecimal.valueOf(50_000_000)));
        when(accountService.updateAccountInfo("oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23)))
                .thenReturn(new AccountUpdateResponse("oleg", "Oleg Tarasov", LocalDate.of(1997, 3, 23)));
        when(accountService.getAccountInfo("oleg"))
                .thenReturn(new AccountResponse("Oleg Tarasov", LocalDate.of(1997, 3, 23), BigDecimal.valueOf(500_000_000),
                        List.of(new AccountDto("ivanov", "Pavel Ivanov"),
                                new AccountDto("bannova", "Nastya Bannova"))));
    }
}
