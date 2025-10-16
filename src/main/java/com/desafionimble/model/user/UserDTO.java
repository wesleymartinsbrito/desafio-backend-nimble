package com.desafionimble.model.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Modelo de dados para criar ou visualizar um usuário")
public class UserDTO {
    @Schema(description = "ID único do usuário", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "Nome do usuário", example = "Usuário")
    private String name;
    @Schema(description = "CPF do usuário", example = "12345678900")
    private String cpf;
    @Schema(description = "Email do usuário", example = "usuario@teste.com")
    private String email;
    @Schema(description = "Senha do usuário")
    private String password;
    @Schema(description = "Saldo do usuário", example = "99.90")
    private BigDecimal balance;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
