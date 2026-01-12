package com.tarasov.bank.frontend.controller;

import com.tarasov.bank.frontend.client.ApiGatewayClient;
import com.tarasov.bank.frontend.dto.AccountUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
@Validated
public class BankController {

    private final ApiGatewayClient apiGatewayClient;

    @GetMapping("/")
    public String index() {
        System.out.println("Auth: " + SecurityContextHolder.getContext().getAuthentication());
        return "main";
    }

    @PostMapping("/account")
    public String updateAccountDetails(@Valid AccountUpdateRequest request) {
        String response = apiGatewayClient.post("/account")
                .body(request)
                .retrieve()
                .body(String.class);
        System.out.println(response);
        return "redirect:/";
    }

    @PostMapping("/cash")
    public String updateBalance() {
        System.out.println("Auth: " + SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @PostMapping("/transfer")
    public String transferMoney() {
        System.out.println("Auth: " + SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }
}
