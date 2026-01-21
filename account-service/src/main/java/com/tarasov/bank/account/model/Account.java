package com.tarasov.bank.account.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;


@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String login;

    @Column(nullable = false)
    String fullName;

    @Column
    LocalDate birthdate;

    @Column(nullable = false)
    BigDecimal balance;

    @Version
    Instant lastUpdated;

    public Account(String login, String fullName, LocalDate birthdate, BigDecimal balance) {
        this.login = login;
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.balance = balance;
    }
}
