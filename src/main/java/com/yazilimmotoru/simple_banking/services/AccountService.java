package com.yazilimmotoru.simple_banking.services;

import com.yazilimmotoru.simple_banking.model.Account;
import com.yazilimmotoru.simple_banking.model.BillPaymentTransaction;
import com.yazilimmotoru.simple_banking.model.DepositTransaction;
import com.yazilimmotoru.simple_banking.model.WithdrawalTransaction;
import com.yazilimmotoru.simple_banking.model.exception.AccountNotFoundException;
import com.yazilimmotoru.simple_banking.model.exception.InsufficientBalanceException;
import com.yazilimmotoru.simple_banking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public void credit(String accountNumber, double amount) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        try {
            account.post(new DepositTransaction(amount));
            accountRepository.save(account);
        } catch (InsufficientBalanceException e) {
            throw new RuntimeException("Deposit failed: " + e.getMessage(), e);
        }
    }

    public void debit(String accountNumber, double amount) throws InsufficientBalanceException {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        account.post(new WithdrawalTransaction(amount));
        accountRepository.save(account);
    }


    public void billPayment(String accountNumber, String payee, double amount) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        try {
            account.post(new BillPaymentTransaction(payee, amount));
            accountRepository.save(account);
        } catch (InsufficientBalanceException e) {
            throw new RuntimeException("Bill payment failed: " + e.getMessage(), e);
        }
    }

    public Account getAccount(String accountNumber) {
        return accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

}
