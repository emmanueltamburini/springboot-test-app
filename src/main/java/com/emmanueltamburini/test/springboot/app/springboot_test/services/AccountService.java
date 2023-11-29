package com.emmanueltamburini.test.springboot.app.springboot_test.services;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;

import java.math.BigDecimal;

public interface AccountService {
    Account findById(Long id);
    int checkTotalTransfer(Long bankId);
    BigDecimal checkAmount(Long accountId);
    void transfer(Long origenAccountId, Long targetAccountId, BigDecimal amount, Long bankId);
}
