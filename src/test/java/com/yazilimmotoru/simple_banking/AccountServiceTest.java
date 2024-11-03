package com.yazilimmotoru.simple_banking;


import com.yazilimmotoru.simple_banking.model.Account;
import com.yazilimmotoru.simple_banking.model.exception.AccountNotFoundException;
import com.yazilimmotoru.simple_banking.model.exception.InsufficientBalanceException;
import com.yazilimmotoru.simple_banking.repository.AccountRepository;
import com.yazilimmotoru.simple_banking.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        account = new Account("Jim", "12345");
        account.setBalance(1000.0);
    }

    @Test
    public void testCredit() {
        when(accountRepository.findById("12345")).thenReturn(java.util.Optional.of(account));

        accountService.credit("12345", 500.0);

        assertEquals(1500.0, account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void testDebit_SufficientBalance() throws InsufficientBalanceException {
        when(accountRepository.findById("12345")).thenReturn(java.util.Optional.of(account));

        accountService.debit("12345", 200.0);

        assertEquals(800.0, account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }


    @Test
    public void testDebit_InsufficientBalance() {
        when(accountRepository.findById("12345")).thenReturn(java.util.Optional.of(account));

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            accountService.debit("12345", 1200.0);
        });

        assertEquals("Insufficient funds.", exception.getMessage());
    }

    @Test
    public void testAccountNotFound() {
        when(accountRepository.findById("99999")).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccount("99999");
        });

        assertEquals("Account not found with number: 99999", exception.getMessage());
    }
}
