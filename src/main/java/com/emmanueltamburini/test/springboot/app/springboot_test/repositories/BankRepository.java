package com.emmanueltamburini.test.springboot.app.springboot_test.repositories;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
    List<Bank> findAll();
    Optional<Bank> findById(Long id);
    Bank save(Bank bank);
}
