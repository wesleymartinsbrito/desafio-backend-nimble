package com.desafionimble.service;

import com.desafionimble.model.dtos.AuthorizerResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UtilsService {

    private final RestTemplate restTemplate;

    @Value("${external.authorizer.url}")
    private String authorizerUrl;

    public UtilsService(
            RestTemplate restTemplate
    ){
        this.restTemplate = restTemplate;
    }

    public boolean autorizacao() {
        try {
            ResponseEntity<AuthorizerResponseDTO> response =
                    restTemplate.getForEntity(authorizerUrl, AuthorizerResponseDTO.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getData().isAuthorized();
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }
}
