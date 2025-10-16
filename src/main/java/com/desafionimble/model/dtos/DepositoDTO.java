package com.desafionimble.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Modelo de dados para criar ou visualizar um depósito")
public class DepositoDTO {
    @Schema(description = "Cpf do usuário que vai receber o depósito", example = "12345678900")
    private String cpf;
    @Schema(description = "Valor do depósito", example = "99.90")
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
