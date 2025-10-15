package com.desafionimble.model.dtos;

public class AuthorizerResponseDTO {
    private String status;
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
