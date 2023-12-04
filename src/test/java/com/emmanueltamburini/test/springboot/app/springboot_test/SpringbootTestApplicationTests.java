package com.emmanueltamburini.test.springboot.app.springboot_test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.emmanueltamburini.test.springboot.app.springboot_test.exceptions.InsufficientMoneyException;
import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;
import com.emmanueltamburini.test.springboot.app.springboot_test.models.Bank;
import com.emmanueltamburini.test.springboot.app.springboot_test.repositories.AccountRepository;
import com.emmanueltamburini.test.springboot.app.springboot_test.repositories.BankRepository;
import com.emmanueltamburini.test.springboot.app.springboot_test.services.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class SpringbootTestApplicationTests {
	@MockBean
	private AccountRepository accountRepository;
	@MockBean
	private BankRepository bankRepository;
	@Autowired
	private AccountServiceImpl accountService;

	@Test
	void testTransferAccount() {
		when(accountRepository.findById(1L)).thenReturn(Data.ACCOUNT_1());
		when(accountRepository.findById(2L)).thenReturn(Data.ACCOUNT_2());
		when(bankRepository.findById(1L)).thenReturn(Data.BANK());

		BigDecimal origenAmount = accountService.checkAmount(1L);
		BigDecimal targetAmount = accountService.checkAmount(2L);
		assertEquals("1000", origenAmount.toPlainString());
		assertEquals("2000", targetAmount.toPlainString());

		accountService.transfer(1L, 2L, new BigDecimal("100"), 1L);

		origenAmount = accountService.checkAmount(1L);
		targetAmount = accountService.checkAmount(2L);
		assertEquals("900", origenAmount.toPlainString());
		assertEquals("2100", targetAmount.toPlainString());

		final int total = accountService.checkTotalTransfer(1L);
		assertEquals(1, total);

		verify(accountRepository, times(3)).findById(1L);
		verify(accountRepository, times(3)).findById(2L);
		verify(accountRepository, times(2)).save(any(Account.class));

		verify(bankRepository, times(2)).findById(1L);
		verify(bankRepository).save(any(Bank.class));

		verify(accountRepository, times(6)).findById(anyLong());
		verify(accountRepository, never()).findAll();
	}


	@Test
	void testTransferIncorrectAmountAccount() {
		when(accountRepository.findById(1L)).thenReturn(Data.ACCOUNT_1());
		when(accountRepository.findById(2L)).thenReturn(Data.ACCOUNT_2());
		when(bankRepository.findById(1L)).thenReturn(Data.BANK());

		BigDecimal origenAmount = accountService.checkAmount(1L);
		BigDecimal targetAmount = accountService.checkAmount(2L);
		assertEquals("1000", origenAmount.toPlainString());
		assertEquals("2000", targetAmount.toPlainString());

		assertThrows(InsufficientMoneyException.class, () -> {
			accountService.transfer(1L, 2L, new BigDecimal("1200"), 1L);
		});

		assertEquals("1000", origenAmount.toPlainString());
		assertEquals("2000", targetAmount.toPlainString());

		final int total = accountService.checkTotalTransfer(1L);
		assertEquals(0, total);

		verify(accountRepository, times(2)).findById(1L);
		verify(accountRepository, times(1)).findById(2L);
		verify(accountRepository, never()).save(any(Account.class));

		verify(bankRepository, times(1)).findById(1L);
		verify(bankRepository, never()).save(any(Bank.class));

		verify(accountRepository, times(3)).findById(anyLong());
		verify(accountRepository, never()).findAll();
	}

	@Test
	void testAssertSameElement() {
		when(accountRepository.findById(1L)).thenReturn(Data.ACCOUNT_1());

		final Account account1 = accountService.findById(1L);
		final Account account2 = accountService.findById(1L);

		assertSame(account1, account2);
		assertEquals("PERSON TEST 1", account1.getPerson());
		assertEquals("PERSON TEST 1", account2.getPerson());

		verify(accountRepository, times(2)).findById(1L);
	}

	@Test
	void testFindAll() {
		when(accountRepository.findAll()).thenReturn(Data.LIST_ACCOUNT());

		final List<Account> accounts = accountService.findAll();

		assertFalse(accounts.isEmpty());
		assertEquals(2, accounts.size());
		assertTrue(accounts.contains(Data.ACCOUNT_1().orElseThrow()));
		assertTrue(accounts.contains(Data.ACCOUNT_2().orElseThrow()));
		verify(accountRepository).findAll();
	}

	@Test
	void testSaveAccount() {
		final Account account = new Account(null, "PERSON TEST 3", new BigDecimal("3000"));

		when(accountRepository.save(any(Account.class))).then(invocation -> {
			final Account c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});

		final Account savedAccount = accountService.save(account);

		assertEquals("PERSON TEST 3", savedAccount.getPerson());
		assertEquals(3, savedAccount.getId());
		assertEquals("3000", account.getAmount().toPlainString());

		verify(accountRepository).save(account);
	}
}
