package com.emmanueltamburini.test.springboot.app.springboot_test.controllers;

import com.emmanueltamburini.test.springboot.app.springboot_test.Data;
import com.emmanueltamburini.test.springboot.app.springboot_test.models.TransactionDto;
import com.emmanueltamburini.test.springboot.app.springboot_test.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AccountService accountService;
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }
    @Test
    void testDetails() throws Exception {
        when(accountService.findById(1L)).thenReturn(Data.ACCOUNT_1().orElseThrow());

        mvc.perform(get("/api/account/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person").value("PERSON TEST 1"))
                .andExpect(jsonPath("$.amount").value("1000"));

        verify(accountService).findById(1L);
    }

    @Test
    void testTransfer() throws Exception {
        final TransactionDto dto = new TransactionDto();
        dto.setOrigenAccountId(1L);
        dto.setTargetAccountId(2L);
        dto.setAmount(new BigDecimal("100"));
        dto.setBankId(1L);

        final Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transfer was success");
        response.put("transaction", dto);

        mvc.perform(post("/api/account/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Transfer was success"))
                .andExpect(jsonPath("$.transaction.origenAccountId").value(dto.getOrigenAccountId()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}