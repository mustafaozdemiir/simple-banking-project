package com.yazilimmotoru.simple_banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yazilimmotoru.simple_banking.controller.AccountController;
import com.yazilimmotoru.simple_banking.model.Account;
import com.yazilimmotoru.simple_banking.model.exception.InsufficientBalanceException;
import com.yazilimmotoru.simple_banking.repository.AccountRepository;
import com.yazilimmotoru.simple_banking.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

import java.util.Collections;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account("Mustafa", "123456");
        account.setBalance(100.0);
    }

    @Test
    public void testCredit() throws Exception {
        double amount = 50.0;

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(Collections.singletonMap("amount", amount));

        mockMvc.perform(post("/account/v1/credit/123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));

        verify(accountService, times(1)).credit(any(), eq(amount));
    }

    @Test
    public void testDebitWithSufficientBalance() throws Exception {
        double amount = 50.0;

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(Collections.singletonMap("amount", amount));

        mockMvc.perform(post("/account/v1/debit/123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));

        verify(accountService, times(1)).debit(any(), eq(amount));
    }

    @Test
    public void testDebitWithInsufficientBalance() throws Exception {
        double amount = 150.0;
        doThrow(new InsufficientBalanceException("Insufficient funds.")).when(accountService).debit(any(), eq(amount));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(Collections.singletonMap("amount", amount));

        mockMvc.perform(post("/account/v1/debit/123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andExpect(jsonPath("$.errorMessage").value("Insufficient funds."));

        verify(accountService, times(1)).debit(any(), eq(amount));
    }
}