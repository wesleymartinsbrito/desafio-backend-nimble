package com.desafionimble.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Modelo de dados para visualizar o retorno do autorizador")
public class AuthorizerResponseDTO {
    @Schema(description = "Status do autorizador", example = "success")
    private String status;
    @Schema(description = "Retorno do autorizador", example = "true")
    private ResponseDataDTO data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResponseDataDTO getData() {
        return data;
    }

    public void setData(ResponseDataDTO data) {
        this.data = data;
    }
}
