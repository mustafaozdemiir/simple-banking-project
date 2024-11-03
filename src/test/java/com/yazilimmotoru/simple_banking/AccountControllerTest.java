package com.yazilimmotoru.simple_banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazilimmotoru.simple_banking.controller.AccountController;
import com.yazilimmotoru.simple_banking.model.Account;
import com.yazilimmotoru.simple_banking.model.exception.InsufficientBalanceException;
import com.yazilimmotoru.simple_banking.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account("Jim", "123456");
        account.setBalance(100.0);
    }

    @Test
    public void testCreateAccount() throws Exception {
        when(accountService.createAccount(any(Account.class))).thenReturn(account);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(account);

        mockMvc.perform(post("/account/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner").value("Jim"))
                .andExpect(jsonPath("$.accountNumber").value("123456"));

        verify(accountService, times(1)).createAccount(any(Account.class));
    }

    @Test
    public void testCredit() throws Exception {
        double amount = 50.0;
        Map<String, Double> request = new HashMap<>();
        request.put("amount", amount);

        mockMvc.perform(post("/account/v1/credit/123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));

        verify(accountService, times(1)).credit(eq("123456"), eq(amount));
    }

    @Test
    public void testDebitWithSufficientBalance() throws Exception {
        double amount = 50.0;
        Map<String, Double> request = new HashMap<>();
        request.put("amount", amount);

        mockMvc.perform(post("/account/v1/debit/123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));

        verify(accountService, times(1)).debit(eq("123456"), eq(amount));
    }

    @Test
    public void testDebitWithInsufficientBalance() throws Exception {
        double amount = 150.0;
        Map<String, Double> request = new HashMap<>();
        request.put("amount", amount);

        doThrow(new InsufficientBalanceException("Insufficient funds"))
                .when(accountService).debit(anyString(), eq(amount));

        mockMvc.perform(post("/account/v1/debit/123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.errorMessage").value("Insufficient funds"));

        verify(accountService, times(1)).debit(eq("123456"), eq(amount));
    }

    @Test
    public void testGetAccount() throws Exception {
        when(accountService.getAccount("123456")).thenReturn(account);

        mockMvc.perform(get("/account/v1/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner").value("Jim"))
                .andExpect(jsonPath("$.accountNumber").value("123456"))
                .andExpect(jsonPath("$.balance").value(100.0));

        verify(accountService, times(1)).getAccount("123456");
    }

    @Test
    public void testBillPayment() throws Exception {
        String payee = "Electric Company";
        double amount = 50.0;
        Map<String, Object> request = new HashMap<>();
        request.put("payee", payee);
        request.put("amount", amount);

        mockMvc.perform(post("/account/v1/bill-payment/123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));

        verify(accountService, times(1)).billPayment(eq("123456"), eq(payee), eq(amount));
    }

    @Test
    public void testCreateAccountWithError() throws Exception {
        when(accountService.createAccount(any(Account.class))).thenThrow(new RuntimeException("Error creating account"));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(account);

        mockMvc.perform(post("/account/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.errorMessage").value("Error creating account"));

        verify(accountService, times(1)).createAccount(any(Account.class));
    }

}
