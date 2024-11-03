package com.yazilimmotoru.simple_banking.model;

import com.yazilimmotoru.simple_banking.model.exception.InsufficientBalanceException;

public class BillPaymentTransaction extends Transaction {
    private String payee;

    public BillPaymentTransaction() {
    }

    public BillPaymentTransaction(String payee, double amount) {
        super(amount);
        this.payee = payee;
    }

    @Override
    public void apply(Account account) throws InsufficientBalanceException {
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient funds.");
        }
        account.setBalance(account.getBalance() - amount);
    }

    public String getPayee() {
        return payee;
    }
}