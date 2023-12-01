package com.emmanueltamburini.test.springboot.app.springboot_test.controllers;

import com.emmanueltamburini.test.springboot.app.springboot_test.models.Account;
import com.emmanueltamburini.test.springboot.app.springboot_test.models.TransactionDto;
import com.emmanueltamburini.test.springboot.app.springboot_test.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Account details(@PathVariable Long id) {
        return accountService.findById(id);
    }

    @PostMapping("/transfer")
    @ResponseStatus(OK)
    public ResponseEntity<Map<String, Object>> transfer(@RequestBody TransactionDto transactionDto) {
        accountService.transfer(
                transactionDto.getOrigenAccountId(),
                transactionDto.getTargetAccountId(),
                transactionDto.getAmount(),
                transactionDto.getBankId()
        );

        final Map<String, Object> response = new HashMap<>();

        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("Message", "Transfer was success");
        response.put("transaction", transactionDto);

        doSomething(response);

        return ResponseEntity.ok(response);
    }

    private void doSomething(Map<String, Object> response) {
        response = new HashMap<>();
    }
}