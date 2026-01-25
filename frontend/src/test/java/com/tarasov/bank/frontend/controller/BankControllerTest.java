package com.tarasov.bank.frontend.controller;


import com.tarasov.bank.frontend.controller.advice.FrontendExceptionHandler;
import com.tarasov.bank.frontend.model.*;
import com.tarasov.bank.frontend.security.TestSecurityConfig;
import com.tarasov.bank.frontend.service.BankService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(properties = { "spring.cloud.config.enabled=false" })
@AutoConfigureMockMvc
@Import({TestSecurityConfig.class, FrontendExceptionHandler.class})
public class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    protected BankService bankService;

    @Test
    public void contextLoads() {
    }

    @Test
    @WithUserDetails(value = "oleg", userDetailsServiceBeanName = "testUserDetailsService")
    void mainPageTest() throws Exception {
        List<Account> accountList = List.of(new Account("lena", "Elena T."),
                new Account("trakand", "Venera Trakand"));
        AccountResponse response = new AccountResponse(
                "Oleg Tarasov",
                LocalDate.of(1997, 3, 23),
                BigDecimal.valueOf(1_000_000),
                accountList
        );

        when(bankService.getAccountDetails()).thenReturn(response);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("name", "Oleg Tarasov"))
                .andExpect(model().attribute("birthdate", LocalDate.of(1997, 3, 23)))
                .andExpect(model().attribute("sum", BigDecimal.valueOf(1_000_000)))
                .andExpect(model().attribute("accounts", accountList));
    }

    @Test
    @WithUserDetails(value = "oleg", userDetailsServiceBeanName = "testUserDetailsService")
    void updateAccountDetails_shouldRedirect() throws Exception {
        mockMvc.perform(post("/account")
                        .with(csrf())
                        .param("name", "Oleg")
                        .param("birthdate", "1990-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?info=Account details updated"));

        verify(bankService).updateAccount(any(AccountUpdateRequest.class));
    }

    @Test
    @WithUserDetails(value = "oleg", userDetailsServiceBeanName = "testUserDetailsService")
    void updateBalance_shouldRedirect() throws Exception {
        mockMvc.perform(post("/cash")
                        .with(csrf())
                        .param("value", "100000")
                        .param("action", "PUT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?info=Balance updated"));

        verify(bankService).updateBalance(any(BalanceUpdateRequest.class));
    }

    @Test
    @WithUserDetails(value = "oleg", userDetailsServiceBeanName = "testUserDetailsService")
    void transferMoney_shouldRedirect() throws Exception {
        mockMvc.perform(post("/transfer")
                        .with(csrf())
                        .param("value", "50")
                        .param("login", "polukhina"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?info=Money transferred"));

        verify(bankService).transferMoney(any(MoneyTransferRequest.class));
    }
}
