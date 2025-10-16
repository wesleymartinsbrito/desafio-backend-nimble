package com.desafionimble.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Modelo de dados para visualizar o retorno do login")
public class LoginDTO {
    @Schema(description = "Email ou senha do usuário", example = "email ou senha")
    private String login;
    @Schema(description = "Senha do usuário")
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
