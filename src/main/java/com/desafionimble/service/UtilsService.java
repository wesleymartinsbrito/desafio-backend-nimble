package com.desafionimble.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
public class UtilsService {

    private final RestTemplate restTemplate;

    public UtilsService(
            RestTemplate restTemplate
    ){
        this.restTemplate = restTemplate;
    }

    public boolean autorizacao(){
        ResponseEntity<Map> response = restTemplate.getForEntity("https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer", Map.class);
        return Objects.requireNonNull(response.getBody()).get("status") == "success";
    }
}
