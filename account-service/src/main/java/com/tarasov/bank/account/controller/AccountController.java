package com.tarasov.bank.account.controller;


import com.tarasov.bank.account.dto.AccountUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class AccountController {

    @PostMapping("/account")
    @PreAuthorize("hasAuthority('account.update')")
    public String updateAccount(@RequestBody @Valid AccountUpdateRequest request) {
        return request.toString();
    }
}
