package com.emmanueltamburini.test.springboot.app.springboot_test.repositories;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
