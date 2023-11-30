package com.emmanueltamburini.test.springboot.app.springboot_test.repositories;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select a from Account a where a.person = ?1")
    Optional<Account> findByPerson(String person);
}
