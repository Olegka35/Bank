package com.tarasov.bank.frontend.controller;

import com.tarasov.bank.frontend.model.AccountUpdateRequest;
import com.tarasov.bank.frontend.model.BalanceUpdateRequest;
import com.tarasov.bank.frontend.model.MoneyTransferRequest;
import com.tarasov.bank.frontend.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@Validated
public class BankController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankController.class);

    private final BankService bankService;

    @GetMapping("/")
    public String index() {
        System.out.println("Auth: " + SecurityContextHolder.getContext().getAuthentication());
        return "main";
    }

    @PostMapping("/account")
    public String updateAccountDetails(@Valid AccountUpdateRequest request) {
        bankService.updateAccount(request);
        return "redirect:/";
    }

    @PostMapping("/cash")
    public String updateBalance(@Valid BalanceUpdateRequest request) {
        bankService.updateBalance(request);
        return "redirect:/";
    }

    @PostMapping("/transfer")
    public String transferMoney(MoneyTransferRequest request) {
        bankService.transferMoney(request);
        return "redirect:/";
    }
}
