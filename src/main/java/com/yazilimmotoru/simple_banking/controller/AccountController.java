package com.yazilimmotoru.simple_banking.controller;

import com.yazilimmotoru.simple_banking.model.Account;
import com.yazilimmotoru.simple_banking.model.exception.InsufficientBalanceException;
import com.yazilimmotoru.simple_banking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            Account savedAccount = accountService.createAccount(account);
            return ResponseEntity.ok(savedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TransactionStatus("FAILED", e.getMessage()));
        }
    }

    @PostMapping("/credit/{accountNumber}")
    public ResponseEntity<TransactionStatus> credit(@PathVariable String accountNumber, @RequestBody Map<String, Double> body) {
        try {
            double amount = body.get("amount");
            accountService.credit(accountNumber, amount);
            return ResponseEntity.ok(new TransactionStatus("OK"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TransactionStatus("FAILED", e.getMessage()));
        }
    }

    @PostMapping("/debit/{accountNumber}")
    public ResponseEntity<TransactionStatus> debit(@PathVariable String accountNumber, @RequestBody Map<String, Double> body) {
        try {
            double amount = body.get("amount");
            accountService.debit(accountNumber, amount);
            return ResponseEntity.ok(new TransactionStatus("OK"));
        } catch (InsufficientBalanceException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TransactionStatus("FAILED", e.getMessage()));
        }
    }



    @PostMapping("/bill-payment/{accountNumber}")
    public ResponseEntity<TransactionStatus> billPayment(@PathVariable String accountNumber, @RequestBody Map<String, Object> body) {
        String payee = (String) body.get("payee");
        double amount = (double) body.get("amount");
        try {
            accountService.billPayment(accountNumber, payee, amount);
            return ResponseEntity.ok(new TransactionStatus("OK"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TransactionStatus("FAILED", e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Account account = accountService.getAccount(accountNumber);
        return ResponseEntity.ok(account);
    }
}
