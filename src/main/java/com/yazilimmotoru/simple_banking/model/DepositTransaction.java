package com.yazilimmotoru.simple_banking.model;

import jakarta.persistence.Entity;

@Entity
public class DepositTransaction extends Transaction {
    public DepositTransaction() {
    }

    public DepositTransaction(double amount) {
        super(amount);
    }

    @Override
    public void apply(Account account) {
        account.setBalance(account.getBalance() + amount);
    }
}

