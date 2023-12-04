package com.emmanueltamburini.test.springboot.app.springboot_test.controllers;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.TransactionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AccountControllerWebClientTest {

    @Autowired
    private WebTestClient client;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testIntegrationTransfer() throws JsonProcessingException {
        final TransactionDto dto = new TransactionDto();
        dto.setOrigenAccountId(1L);
        dto.setTargetAccountId(2L);
        dto.setBankId(1L);
        dto.setAmount(new BigDecimal("100"));

        final Map<String, Object> response = new HashMap<>();

        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Transfer was success");
        response.put("transaction", dto);


        client.post().uri("http://localhost:8080/api/account/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").value(is("Transfer was success"))
                .jsonPath("$.message").value(value -> assertEquals("Transfer was success", value))
                .jsonPath("$.message").isEqualTo("Transfer was success")
                .jsonPath("$.transaction.origenAccountId").isEqualTo(dto.getOrigenAccountId())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(objectMapper.writeValueAsString(response));
    }
}