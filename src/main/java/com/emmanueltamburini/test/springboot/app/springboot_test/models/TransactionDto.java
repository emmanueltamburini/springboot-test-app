package com.emmanueltamburini.test.springboot.app.springboot_test.models;

import java.math.BigDecimal;

public class TransactionDto {
    private Long origenAccountId;
    private Long targetAccountId;
    private BigDecimal amount;
    private Long bankId;

    public Long getOrigenAccountId() {
        return origenAccountId;
    }

    public void setOrigenAccountId(Long origenAccountId) {
        this.origenAccountId = origenAccountId;
    }

    public Long getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(Long targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }
}
