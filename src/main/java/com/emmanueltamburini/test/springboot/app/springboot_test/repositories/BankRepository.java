package com.emmanueltamburini.test.springboot.app.springboot_test.repositories;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Bank;

import java.util.List;

public interface BankRepository {
    List<Bank> findAll();
    Bank findById(Long id);
    void save(Bank bank);
}
