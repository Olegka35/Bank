package com.tarasov.bank.transfer.contract;

import com.tarasov.bank.transfer.dto.BalanceResponse;
import com.tarasov.bank.transfer.dto.MoneyTransferRequest;
import com.tarasov.bank.transfer.service.MoneyTransferService;
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
public class BaseTransferContractTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected MoneyTransferService moneyTransferService;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        when(moneyTransferService.transferMoney("oleg", new MoneyTransferRequest(new BigDecimal(50_000), "polukhina")))
                .thenReturn(new BalanceResponse(BigDecimal.valueOf(10_000_000)));
    }
}
