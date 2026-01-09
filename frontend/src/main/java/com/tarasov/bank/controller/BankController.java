package com.tarasov.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class BankController {

    @GetMapping
    public String index() {
        return "main";
    }
}
