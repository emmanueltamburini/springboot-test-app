package com.emmanueltamburini.test.springboot.app.springboot_test;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;
import com.emmanueltamburini.test.springboot.app.springboot_test.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@DataJpaTest
public class JpaIntegrationTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testFindById() {
        final Optional<Account> account = accountRepository.findById(1l);

        assertTrue(account.isPresent());
        assertEquals("PERSON TEST 1", account.orElseThrow().getPerson());
    }

    @Test
    void testFindByPerson() {
        final Optional<Account> account = accountRepository.findByPerson("PERSON TEST 1");

        assertTrue(account.isPresent());
        assertEquals("PERSON TEST 1", account.orElseThrow().getPerson());
        assertEquals("1000.00", account.orElseThrow().getAmount().toPlainString());
    }

    @Test
    void testFindByPersonThrowException() {
        final Optional<Account> account = accountRepository.findByPerson("WRONG PERSON TEST 1");

        assertThrows(NoSuchElementException.class, account::orElseThrow);
        assertFalse(account.isPresent());
    }

    @Test
    void testFindAll() {
        final List<Account> account = accountRepository.findAll();

        assertFalse(account.isEmpty());
        assertEquals(2, account.size());
    }

    @Test
    void testSaveById() {
        final Account account = new Account(null, "PERSON TEST 3", new BigDecimal("3000"));
        final Account savedAccount = accountRepository.save(account);

        //Here it does not make a select because it saved in cache the object when it saved the account, so it is no necessary
        final Account foundAccount = accountRepository.findById(savedAccount.getId()).orElseThrow();

        assertEquals("PERSON TEST 3", foundAccount.getPerson());
        assertEquals("3000", foundAccount.getAmount().toPlainString());
    }

    @Test
    void testSaveByName() {
        final Account account = new Account(null, "PERSON TEST 3", new BigDecimal("3000"));
        accountRepository.save(account);

        final Account foundAccount = accountRepository.findByPerson("PERSON TEST 3").orElseThrow();

        assertEquals("PERSON TEST 3", foundAccount.getPerson());
        assertEquals("3000", foundAccount.getAmount().toPlainString());
    }

    @Test
    void testUpdateById() {
        final Account account = new Account(null, "PERSON TEST 3", new BigDecimal("3000"));
        final Account savedAccount = accountRepository.save(account);

        final Account foundAccount = accountRepository.findById(savedAccount.getId()).orElseThrow();

        assertEquals("PERSON TEST 3", foundAccount.getPerson());
        assertEquals("3000", foundAccount.getAmount().toPlainString());

        savedAccount.setAmount(new BigDecimal("3800"));
        final Account updatedAccount = accountRepository.save(savedAccount);

        assertEquals("PERSON TEST 3", updatedAccount.getPerson());
        assertEquals("3800", updatedAccount.getAmount().toPlainString());
    }

    @Test
    void testDelete() {
        final Account foundAccount = accountRepository.findById(2L).orElseThrow();
        assertEquals("PERSON TEST 2", foundAccount.getPerson());

        accountRepository.delete(foundAccount);

        final Optional<Account> deletedAccount = accountRepository.findByPerson("PERSON TEST 2");

        assertThrows(NoSuchElementException.class, deletedAccount::orElseThrow);
        assertEquals(1, accountRepository.findAll().size());
    }
}
