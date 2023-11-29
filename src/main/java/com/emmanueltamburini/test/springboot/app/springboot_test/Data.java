package com.emmanueltamburini.test.springboot.app.springboot_test;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;
import com.emmanueltamburini.test.springboot.app.springboot_test.models.Bank;

import java.math.BigDecimal;

public class Data {
    public static final Account ACCOUNT_1 () {return new Account(1L, "ACCOUNT TEST 1", new BigDecimal("1000"));}
    public static final Account ACCOUNT_2 () {return new Account(1L, "ACCOUNT TEST 2", new BigDecimal("2000"));}
    public static final Bank BANK () {return new Bank(1L, "BANK TEST", 0);}

}
