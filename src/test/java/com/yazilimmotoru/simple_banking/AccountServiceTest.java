package com.yazilimmotoru.simple_banking;

import com.yazilimmotoru.simple_banking.model.Account;
import com.yazilimmotoru.simple_banking.model.exception.InsufficientBalanceException;
import com.yazilimmotoru.simple_banking.repository.AccountRepository;
import com.yazilimmotoru.simple_banking.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account("Mustafa", "123456");
        account.setBalance(100.0);
    }

    @Test
    public void testCredit() {
        double amount = 50.0;

        when(accountRepository.findById("123456")).thenReturn(Optional.of(account));

        accountService.credit("123456", amount);

        assertEquals(150.0, account.getBalance());
    }

    @Test
    public void testDebitWithSufficientBalance() throws InsufficientBalanceException {
        double amount = 50.0;

        when(accountRepository.findById("123456")).thenReturn(Optional.of(account));

        accountService.debit("123456", amount);

        assertEquals(50.0, account.getBalance());
    }

    @Test
    public void testDebitWithInsufficientBalance() {
        double amount = 150.0;

        when(accountRepository.findById("123456")).thenReturn(Optional.of(account));

        Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
            accountService.debit("123456", amount);
        });

        assertEquals("Insufficient funds.", exception.getMessage());
    }

    @Test
    public void testBillPaymentWithSufficientBalance() throws InsufficientBalanceException {
        double amount = 30.0;

        when(accountRepository.findById("123456")).thenReturn(Optional.of(account));

        accountService.billPayment("123456", "Utility Company", amount);

        assertEquals(70.0, account.getBalance());
    }

    @Test
    public void testBillPaymentWithInsufficientBalance() {
        double amount = 150.0;

        when(accountRepository.findById("123456")).thenReturn(Optional.of(account));

        Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
            accountService.billPayment("123456", "Utility Company", amount);
        });

        assertEquals("Insufficient funds.", exception.getMessage());
    }
}
