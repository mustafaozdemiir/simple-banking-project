package com.yazilimmotoru.simple_banking.model;

import com.yazilimmotoru.simple_banking.model.exception.InsufficientBalanceException;
import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    protected double amount;
    protected LocalDateTime date;

    @ManyToOne
    private Account account;

    public Transaction() {
    }

    public Transaction(double amount) {
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public abstract void apply(Account account) throws InsufficientBalanceException;
}
