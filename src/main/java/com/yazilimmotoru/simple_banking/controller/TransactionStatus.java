package com.yazilimmotoru.simple_banking.controller;

import java.util.UUID;

public class TransactionStatus {
    private String status;
    private String approvalCode;
    private String errorMessage;

    public TransactionStatus(String status) { //Success
        this.status = status;
        this.approvalCode = UUID.randomUUID().toString();
    }

    public TransactionStatus(String status, String errorMessage) { // Error
        this.status = status;
        this.errorMessage = errorMessage;
        this.approvalCode = UUID.randomUUID().toString();
    }

    public String getStatus() {
        return status;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}