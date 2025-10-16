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

    public static boolean isCpfValido(String cpf) {
        String cpfNumerico = cpf.replaceAll("[^0-9]", "");
        if (cpfNumerico.length() != 11) {
            return false;
        }
        if (cpfNumerico.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpfNumerico.charAt(i) - '0') * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito > 9) {
                primeiroDigito = 0;
            }

            if ((cpfNumerico.charAt(9) - '0') != primeiroDigito) {
                return false;
            }

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpfNumerico.charAt(i) - '0') * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito > 9) {
                segundoDigito = 0;
            }

            return (cpfNumerico.charAt(10) - '0') == segundoDigito;

        } catch (Exception e) {
            return false;
        }
    }
}
