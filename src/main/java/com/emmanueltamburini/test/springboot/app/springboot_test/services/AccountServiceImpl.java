package com.emmanueltamburini.test.springboot.app.springboot_test.services;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;
import com.emmanueltamburini.test.springboot.app.springboot_test.models.Bank;
import com.emmanueltamburini.test.springboot.app.springboot_test.repositories.AccountRepository;
import com.emmanueltamburini.test.springboot.app.springboot_test.repositories.BankRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private BankRepository bankRepository;
    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }
    @Override
    public Account findById(Long id) throws NoSuchElementException {
        return accountRepository.findById(id).orElseThrow();
    }

    @Override
    public int checkTotalTransfer(Long bankId) throws NoSuchElementException {
        final Bank bank = bankRepository.findById(bankId).orElseThrow();
        return bank.getTotalTransfer();
    }

    @Override
    public BigDecimal checkAmount(Long accountId) throws NoSuchElementException {
        final Account account = accountRepository.findById(accountId).orElseThrow();
        return account.getAmount();
    }

    @Override
    public void transfer(Long origenAccountId, Long targetAccountId, BigDecimal amount, Long bankId) throws NoSuchElementException {
        final Account origenAccount = accountRepository.findById(origenAccountId).orElseThrow();
        origenAccount.debit(amount);
        accountRepository.save(origenAccount);

        final Account targetAccount = accountRepository.findById(targetAccountId).orElseThrow();
        targetAccount.credit(amount);
        accountRepository.save(targetAccount);

        final Bank bank = bankRepository.findById(bankId).orElseThrow();
        int totalTransfer = bank.getTotalTransfer();
        bank.setTotalTransfer(++totalTransfer);
        bankRepository.save(bank);
    }
}
