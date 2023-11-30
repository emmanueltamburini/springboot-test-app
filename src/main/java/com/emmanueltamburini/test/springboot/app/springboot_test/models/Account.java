package com.emmanueltamburini.test.springboot.app.springboot_test.models;

import com.emmanueltamburini.test.springboot.app.springboot_test.exceptions.InsufficientMoneyException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name="accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String person;
    private BigDecimal amount;

    public Account() {
    }

    public Account(Long id, String person, BigDecimal amount) {
        this.id = id;
        this.person = person;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void debit (final BigDecimal amount) {
        final BigDecimal newAmount = this.amount.subtract(amount);

        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientMoneyException("Insufficient money to make this action");
        }

        this.amount = newAmount;
    }

    public void credit (final BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(person, account.person) && Objects.equals(amount, account.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, amount);
    }
}
