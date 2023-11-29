package com.emmanueltamburini.test.springboot.app.springboot_test.services;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;
import com.emmanueltamburini.test.springboot.app.springboot_test.models.Bank;
import com.emmanueltamburini.test.springboot.app.springboot_test.repositories.AccountRepository;
import com.emmanueltamburini.test.springboot.app.springboot_test.repositories.BankRepository;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private BankRepository bankRepository;
    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }
    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public int checkTotalTransfer(Long bankId) {
        final Bank bank = bankRepository.findById(bankId);
        return bank.getTotalTransfer();
    }

    @Override
    public BigDecimal checkAmount(Long accountId) {
        final Account account = accountRepository.findById(accountId);
        return account.getAmount();
    }

    @Override
    public void transfer(Long origenAccountId, Long targetAccountId, BigDecimal amount, Long bankId) {
        final Account origenAccount = accountRepository.findById(origenAccountId);
        origenAccount.debit(amount);
        accountRepository.save(origenAccount);

        final Account targetAccount = accountRepository.findById(targetAccountId);
        targetAccount.credit(amount);
        accountRepository.save(targetAccount);

        final Bank bank = bankRepository.findById(bankId);
        int totalTransfer = bank.getTotalTransfer();
        bank.setTotalTransfer(++totalTransfer);
        bankRepository.save(bank);
    }
}
