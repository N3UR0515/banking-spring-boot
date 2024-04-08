package com.banking.bankingapp;

import com.banking.bankingapp.bankaccount.BankingAccount;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankingAppApplicationTests {

	@Test
	@DisplayName("Deposit test")
	void deposit() throws Exception {
		BankingAccount bankAccount = new BankingAccount(0, "aaa", 1);
		float toDeposit = 100;
		bankAccount.deposit(toDeposit);
		assertEquals(bankAccount.getBalance(), toDeposit);
		float toDeposit2 = 150;
		bankAccount.deposit(toDeposit2);
		assertEquals(bankAccount.getBalance(), toDeposit+toDeposit2);
	}

	@Test
	@DisplayName("Deposit minus amount test")
	void depositMinusAmount() throws Exception {
		BankingAccount bankAccount = new BankingAccount(0, "aaa", 1);
		float toDeposit = -100;

		assertThrows(Exception.class, ()->bankAccount.deposit(toDeposit), "Invalid amount provided");
	}

	@Test
	@DisplayName("Withdraw test")
	void withdraw() throws Exception {
		float initialSum = 100;
		BankingAccount bankAccount = new BankingAccount(initialSum, "aaa", 1);
		float toWithdraw = 50;
		bankAccount.withdraw(toWithdraw);
		assertEquals(bankAccount.getBalance(), initialSum - toWithdraw);

		bankAccount.withdraw(bankAccount.getBalance());
		assertEquals(bankAccount.getBalance(), 0);
	}

	@Test
	@DisplayName("Withdraw minus amount test")
	void withdrawMinusAmount() throws Exception {
		BankingAccount bankAccount = new BankingAccount(0, "aaa", 1);
		float toWithdraw = -100;

		assertThrows(Exception.class, ()->bankAccount.withdraw(toWithdraw), "Invalid amount provided");
	}

	@Test
	@DisplayName("Transfer test")
	void transfer() throws Exception {
		float initialSum = 100;
		BankingAccount bankAccount1 = new BankingAccount(initialSum, "aaa", 1);
		BankingAccount bankAccount2 = new BankingAccount(0, "bbb", 2);

		float toTransfer = 50;
		bankAccount1.transfer(bankAccount2, toTransfer);
		assertAll(()->assertEquals(bankAccount1.getBalance(), initialSum - toTransfer),
				()->assertEquals(bankAccount2.getBalance(), toTransfer));
	}

	@Test
	@DisplayName("get password test")
	void getPassword() {
		String passwordToSet = "password";
		BankingAccount bankAccount = new BankingAccount(0, passwordToSet, 1);
		assertEquals(bankAccount.getPassword(), passwordToSet);
	}

	@Test
	@DisplayName("get account number test")
	void getAccountNumber() {
		int idToSet = 1111;
		BankingAccount bankAccount = new BankingAccount(0, "", idToSet);
		assertEquals(bankAccount.getId(), idToSet);
	}

}
