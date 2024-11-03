package com.yazilimmotoru.simple_banking.model;

import com.yazilimmotoru.simple_banking.model.exception.InsufficientBalanceException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    private String accountNumber;
    private String owner;
    private double balance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();


    public Account() {
    }

    public Account(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.balance = 0.0;
    }

    public void post(Transaction transaction) throws InsufficientBalanceException {
        transaction.setAccount(this);
        transaction.apply(this);
        transactions.add(transaction);
    }


    public String getOwner() {
        return owner;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
