package com.emmanueltamburini.test.springboot.app.springboot_test.repositories;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;

import java.util.List;

public interface AccountRepository {
    List<Account> findAll();
    Account findById(Long id);
    void save(Account account);
}
