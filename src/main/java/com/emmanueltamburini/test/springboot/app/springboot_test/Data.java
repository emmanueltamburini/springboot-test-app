package com.emmanueltamburini.test.springboot.app.springboot_test;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;
import com.emmanueltamburini.test.springboot.app.springboot_test.models.Bank;

import java.math.BigDecimal;
import java.util.Optional;

public class Data {
    public static final Optional<Account> ACCOUNT_1 () {return Optional.of(new Account(1L, "PERSON TEST 1", new BigDecimal("1000")));}
    public static final Optional<Account> ACCOUNT_2 () {return Optional.of(new Account(1L, "PERSON TEST 2", new BigDecimal("2000")));}
    public static final Optional<Bank> BANK () {return Optional.of(new Bank(1L, "BANK TEST", 0));}

}
