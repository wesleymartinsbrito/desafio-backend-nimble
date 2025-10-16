package com.desafionimble.model.dtos;

import java.math.BigDecimal;

public class DepositoDTO {
    private String cpf;
    private BigDecimal value;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
