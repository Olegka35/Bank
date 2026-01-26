package com.tarasov.bank.notification.contract;

import com.tarasov.bank.notification.service.NotificationProcessService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;



@WebMvcTest(properties = { "spring.cloud.config.enabled=false" })
@AutoConfigureMockMvc
@ActiveProfiles("contract-test")
@Import(FilterChainConfiguration.class)
public class BaseNotificationContractTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    private NotificationProcessService notificationProcessService;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);

    }
}
