package com.emmanueltamburini.test.springboot.app.springboot_test.controllers;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.TransactionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testIntegrationTransfer() {
        final TransactionDto dto = new TransactionDto();
        dto.setOrigenAccountId(1L);
        dto.setTargetAccountId(2L);
        dto.setBankId(1L);
        dto.setAmount(new BigDecimal("100"));

        final ResponseEntity<String> response = client.
                postForEntity("/api/account/transfer", dto, String.class);

        final String json = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("Transfer was success"));
    }

    @Test
    @Order(2)
    void testIntegrationWithPortTransfer() {
        final TransactionDto dto = new TransactionDto();
        dto.setOrigenAccountId(1L);
        dto.setTargetAccountId(2L);
        dto.setBankId(1L);
        dto.setAmount(new BigDecimal("100"));

        final ResponseEntity<String> response = client.
                postForEntity(createUri("/api/account/transfer"), dto, String.class);

        System.out.println(createUri("/api/account/transfer"));

        final String json = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("Transfer was success"));
    }

    private String createUri(String uri) {
        return "http://localhost:" + port + uri;
    }
}