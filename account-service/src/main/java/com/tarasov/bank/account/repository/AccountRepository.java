package com.tarasov.bank.account.repository;

import com.tarasov.bank.account.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    boolean existsByLogin(String login);
    Account findByLogin(String login);
}
