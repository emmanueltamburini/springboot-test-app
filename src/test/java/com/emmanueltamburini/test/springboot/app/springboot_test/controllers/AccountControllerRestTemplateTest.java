package com.emmanueltamburini.test.springboot.app.springboot_test.controllers;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;
import com.emmanueltamburini.test.springboot.app.springboot_test.models.TransactionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerRestTemplateTest {

    @Autowired
    private TestRestTemplate client;

    private ObjectMapper objectMapper;

    @LocalServerPort //It works for AccountControllerWebClientTest too, to know the port
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
    void testIntegrationWithPortTransfer() throws JsonProcessingException {
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

        final JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals("Transfer was success", jsonNode.path("message").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaction").path("amount").asText());
        assertEquals(1L, jsonNode.path("transaction").path("origenAccountId").asLong());

        final Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("date", LocalDate.now().toString());
        responseBody.put("status", "OK");
        responseBody.put("message", "Transfer was success");
        responseBody.put("transaction", dto);

        assertEquals(objectMapper.writeValueAsString(responseBody), json);
    }

    @Test
    @Order(3)
    void testIntegrationDetail() {
        final ResponseEntity<Account> response = client.getForEntity(createUri("/api/account/1"), Account.class);
        final Account account = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertEquals(1L, account.getId());
        assertEquals("PERSON TEST 1", account.getPerson());
        assertEquals("800.00", account.getAmount().toPlainString());
        assertEquals(new Account(1L, "PERSON TEST 1", new BigDecimal("800.00")), account);
    }

    @Test
    @Order(4)
    void testIntegrationGetAll() throws JsonProcessingException {
        final ResponseEntity<Account[]> response = client.getForEntity(createUri("/api/account"), Account[].class);
        final List<Account> accountList = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertEquals(2, accountList.size());
        assertEquals(1L, accountList.get(0).getId());
        assertEquals("PERSON TEST 1", accountList.get(0).getPerson());
        assertEquals("800.00", accountList.get(0).getAmount().toPlainString());
        assertEquals(2L, accountList.get(1).getId());
        assertEquals("PERSON TEST 2", accountList.get(1).getPerson());
        assertEquals("2200.00", accountList.get(1).getAmount().toPlainString());

        final JsonNode node = objectMapper.readTree(objectMapper.writeValueAsString(accountList));
        assertEquals(1L, node.get(0).path("id").asLong());
        assertEquals("PERSON TEST 1", node.get(0).path("person").asText());
        assertEquals("800.0", node.get(0).path("amount").asText());
        assertEquals(2L, node.get(1).path("id").asLong());
        assertEquals("PERSON TEST 2", node.get(1).path("person").asText());
        assertEquals("2200.0", node.get(1).path("amount").asText());
    }

    @Test
    @Order(5)
    void testIntegrationSave() {
        final Account account = new Account(null, "PERSON TEST 3", new BigDecimal("3000"));
        final ResponseEntity<Account> response = client.postForEntity(createUri("/api/account"), account, Account.class);
        final Account responseAccount = response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertNotNull(responseAccount);
        assertEquals(3L, responseAccount.getId());
        assertEquals("PERSON TEST 3", responseAccount.getPerson());
        assertEquals("3000", responseAccount.getAmount().toPlainString());
    }

    private String createUri(String uri) {
        return "http://localhost:" + port + uri;
    }
}