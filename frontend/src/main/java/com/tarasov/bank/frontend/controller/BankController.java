package com.tarasov.bank.frontend.controller;

import com.tarasov.bank.frontend.model.AccountResponse;
import com.tarasov.bank.frontend.model.AccountUpdateRequest;
import com.tarasov.bank.frontend.model.BalanceUpdateRequest;
import com.tarasov.bank.frontend.model.MoneyTransferRequest;
import com.tarasov.bank.frontend.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public String index(@RequestParam(value = "errors", required = false) String[] errors,
                        @RequestParam(value = "info", required = false) String info,
                        Model model) {
        try {
            LOGGER.info("Incoming request: GET /, errors: {}, info: {}", errors, info);
            AccountResponse response = bankService.getAccountDetails();
            if (response != null) {
                model.addAttribute("name", response.name());
                model.addAttribute("birthdate", response.birthdate());
                model.addAttribute("sum", response.sum());
                model.addAttribute("accounts", response.accounts());
            }
            model.addAttribute("errors", errors);
            model.addAttribute("info", info);
            LOGGER.info("Response: {}", response);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            model.addAttribute("errors", "Unable to load account information");
        }
        return "main";
    }

    @PostMapping("/account")
    public String updateAccountDetails(@Valid AccountUpdateRequest request) {
        LOGGER.info("Incoming request: POST /account,  {}", request);
        bankService.updateAccount(request);
        LOGGER.info("POST /account processed");
        return "redirect:/?info=Account details updated";
    }

    @PostMapping("/cash")
    public String updateBalance(@Valid BalanceUpdateRequest request) {
        LOGGER.info("Incoming request: POST /cash,  {}", request);
        bankService.updateBalance(request);
        LOGGER.info("POST /cash processed");
        return "redirect:/?info=Balance updated";
    }

    @PostMapping("/transfer")
    public String transferMoney(MoneyTransferRequest request) {
        LOGGER.info("Incoming request: POST /transfer,  {}", request);
        bankService.transferMoney(request);
        LOGGER.info("POST /transfer processed");
        return "redirect:/?info=Money transferred";
    }
}
