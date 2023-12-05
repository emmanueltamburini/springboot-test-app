package com.emmanueltamburini.test.springboot.app.springboot_test.services;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

public interface AccountService {
    Account findById(Long id) throws NoSuchElementException;
    List<Account> findAll();
    Account save(Account account);
    void deleteById(Long id);
    int checkTotalTransfer(Long bankId) throws NoSuchElementException;
    BigDecimal checkAmount(Long accountId) throws NoSuchElementException;
    void transfer(Long origenAccountId, Long targetAccountId, BigDecimal amount, Long bankId) throws NoSuchElementException;
}
