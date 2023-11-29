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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class SpringbootTestApplicationTests {
	@Mock
	private AccountRepository accountRepository;
	@Mock
	private BankRepository bankRepository;
	@InjectMocks
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
}
