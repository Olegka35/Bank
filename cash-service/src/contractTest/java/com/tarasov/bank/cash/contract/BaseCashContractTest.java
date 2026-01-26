package com.tarasov.bank.cash.contract;

import com.tarasov.bank.cash.dto.Action;
import com.tarasov.bank.cash.dto.BalanceResponse;
import com.tarasov.bank.cash.dto.BalanceUpdateRequest;
import com.tarasov.bank.cash.service.CashService;
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
import static org.mockito.Mockito.when;


@WebMvcTest(properties = { "spring.cloud.config.enabled=false" })
@AutoConfigureMockMvc
@ActiveProfiles("contract-test")
@Import(FilterChainConfiguration.class)
public class BaseCashContractTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected CashService cashService;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        when(cashService.updateBalance("oleg", new BalanceUpdateRequest(BigDecimal.valueOf(500), Action.PUT)))
                .thenReturn(new BalanceResponse(BigDecimal.valueOf(1000)));
    }
}
