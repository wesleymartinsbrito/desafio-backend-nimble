package com.desafionimble.resource;

import com.desafionimble.service.CobrancaService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CobrancaResource {

    private final CobrancaService cobrancaService;

    public CobrancaResource(
            CobrancaService cobrancaService
    ) {
        this.cobrancaService = cobrancaService;
    }
}
