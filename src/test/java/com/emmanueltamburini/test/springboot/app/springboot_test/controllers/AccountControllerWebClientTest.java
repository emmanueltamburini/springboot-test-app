package com.emmanueltamburini.test.springboot.app.springboot_test.controllers;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;
import com.emmanueltamburini.test.springboot.app.springboot_test.models.TransactionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("Integration_web_client")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    void testIntegrationDetails() throws JsonProcessingException {
        final Account account = new Account(1L, "PERSON TEST 1", new BigDecimal("1000"));

        client.get().uri("/api/account/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.person").isEqualTo("PERSON TEST 1")
                .jsonPath("$.amount").isEqualTo(1000)
                .json(objectMapper.writeValueAsString(account));
    }

    @Test
    @Order(2)
    void testIntegrationDetails2() {
        client.get().uri("/api/account/2").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(response -> {
                    final Account account = response.getResponseBody();
                    assertEquals("PERSON TEST 2", account.getPerson());
                    assertEquals("2000.00", account.getAmount().toPlainString());
                });
    }

    @Test
    @Order(3)
    void testIntegrationGetAll() {
        client.get().uri("/api/account").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2))
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].person").isEqualTo("PERSON TEST 1")
                .jsonPath("$[0].amount").isEqualTo(1000)
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].person").isEqualTo("PERSON TEST 2")
                .jsonPath("$[1].amount").isEqualTo(2000);
    }

    @Test
    @Order(4)
    void testIntegrationGetAll2() {
        client.get().uri("/api/account").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .consumeWith(response -> {
                    final List<Account> accountList = response.getResponseBody();
                    assertNotNull(accountList);
                    assertEquals(2, accountList.size());
                    assertEquals(1L, accountList.get(0).getId());
                    assertEquals("PERSON TEST 1", accountList.get(0).getPerson());
                    assertEquals("1000.00", accountList.get(0).getAmount().toPlainString());                    assertEquals(2, accountList.size());
                    assertEquals(2L, accountList.get(1).getId());
                    assertEquals("PERSON TEST 2", accountList.get(1).getPerson());
                    assertEquals("2000.00", accountList.get(1).getAmount().toPlainString());
                })
                .hasSize(2)
                .value(hasSize(2));
    }

    @Test
    @Order(5)
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


        client.post().uri("/api/account/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(entityExchangeResult -> {
                    try {
                        final JsonNode json = objectMapper.readTree(entityExchangeResult.getResponseBody());
                        assertEquals("Transfer was success", json.path("message").asText());
                        assertEquals(1L, json.path("transaction").path("origenAccountId").asLong());
                        assertEquals(LocalDate.now().toString(), json.path("date").asText());
                        assertEquals("100", json.path("transaction").path("amount").asText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").value(is("Transfer was success"))
                .jsonPath("$.message").value(value -> assertEquals("Transfer was success", value))
                .jsonPath("$.message").isEqualTo("Transfer was success")
                .jsonPath("$.transaction.origenAccountId").isEqualTo(dto.getOrigenAccountId())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .json(objectMapper.writeValueAsString(response));
    }

    @Test
    @Order(6)
    void testIntegrationTransferOtherWay() throws JsonProcessingException {
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


        client.post().uri("/api/account/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(entityExchangeResult -> {
                    try {
                        final String jsonStr = entityExchangeResult.getResponseBody();
                        final JsonNode json = objectMapper.readTree(jsonStr);
                        assertEquals("Transfer was success", json.path("message").asText());
                        assertEquals(1L, json.path("transaction").path("origenAccountId").asLong());
                        assertEquals(LocalDate.now().toString(), json.path("date").asText());
                        assertEquals("100", json.path("transaction").path("amount").asText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    @Order(7)
    void testIntegrationSave() {
        final Account account = new Account(null, "PERSON TEST 3", new BigDecimal("3000"));

        client.post().uri("/api/account")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.person").isEqualTo("PERSON TEST 3")
                .jsonPath("$.person").value(is("PERSON TEST 3"))
                .jsonPath("$.amount").isEqualTo(3000);
    }

    @Test
    @Order(8)
    void testIntegrationSave2() {
        final Account account = new Account(null, "PERSON TEST 4", new BigDecimal("3500"));

        client.post().uri("/api/account")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(account)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Account.class)
                .consumeWith(response -> {
                    final Account accountResponse = response.getResponseBody();
                    assertEquals(4L, accountResponse.getId());
                    assertEquals("PERSON TEST 4", accountResponse.getPerson());
                    assertEquals("3500", accountResponse.getAmount().toPlainString());
                });
    }

    @Test
    @Order(9)
    void testDeleteIntegration() {
        client.get().uri("/api/account").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .hasSize(4);

        client.delete().uri("/api/account/4")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        client.get().uri("/api/account").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Account.class)
                .hasSize(3);

        client.get().uri("/api/account/4").exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty();
    }
}