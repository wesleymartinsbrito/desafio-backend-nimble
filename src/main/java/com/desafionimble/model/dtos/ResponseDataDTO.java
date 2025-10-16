package com.desafionimble.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Modelo de dados para visualizar o retorno do autorizador, se é true ou false")
public class ResponseDataDTO {
    @Schema(description = "Retorno do autorizador", example = "true")
    private boolean authorized;

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }
}
