package com.emmanueltamburini.test.springboot.app.springboot_test.models;

import jakarta.persistence.*;
import java.util.Objects;


@Entity
@Table(name="banks")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "total_transfer")
    private int totalTransfer;

    public Bank() {
    }

    public Bank(Long id, String name, int totalTransfer) {
        this.id = id;
        this.name = name;
        this.totalTransfer = totalTransfer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTransfer() {
        return totalTransfer;
    }

    public void setTotalTransfer(int totalTransfer) {
        this.totalTransfer = totalTransfer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return totalTransfer == bank.totalTransfer && Objects.equals(id, bank.id) && Objects.equals(name, bank.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, totalTransfer);
    }
}
