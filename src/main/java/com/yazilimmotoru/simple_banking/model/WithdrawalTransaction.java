package com.yazilimmotoru.simple_banking.model;

import com.yazilimmotoru.simple_banking.model.exception.InsufficientBalanceException;

public class WithdrawalTransaction extends Transaction {

    public WithdrawalTransaction() {
    }
    public WithdrawalTransaction(double amount) {
        super(amount);
    }

    @Override
    public void apply(Account account) throws InsufficientBalanceException {
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient funds.");
        }
        account.setBalance(account.getBalance() - amount);
    }
}