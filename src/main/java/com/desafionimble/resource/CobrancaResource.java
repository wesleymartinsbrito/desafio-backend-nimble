package com.desafionimble.resource;

import com.desafionimble.model.CartaoDTO;
import com.desafionimble.model.cobranca.CobrancaDTO;
import com.desafionimble.service.CobrancaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cobrancas")
public class CobrancaResource {

    private final CobrancaService cobrancaService;

    public CobrancaResource(
            CobrancaService cobrancaService
    ) {
        this.cobrancaService = cobrancaService;
    }

    @PostMapping
    public ResponseEntity<CobrancaDTO> createCobranca(@RequestBody CobrancaDTO cobrancaDTO){
        CobrancaDTO cobranca = cobrancaService.createCobranca(cobrancaDTO);
        return new ResponseEntity<>(cobranca, HttpStatus.CREATED);
    }

    @GetMapping("/receive")
    public ResponseEntity<List<CobrancaDTO>> findCobrancaReceive(@RequestParam String cpf,
                                                                 @RequestParam int status){
        List<CobrancaDTO> cobrancaDTOList = cobrancaService.findReceive(cpf, status);
        return new ResponseEntity<>(cobrancaDTOList, HttpStatus.OK);
    }

    @GetMapping("/send")
    public ResponseEntity<List<CobrancaDTO>> findCobrancaSend(@RequestParam String cpf,
                                                              @RequestParam int status){
        List<CobrancaDTO> cobrancaDTOList = cobrancaService.findSend(cpf, status);
        return new ResponseEntity<>(cobrancaDTOList, HttpStatus.OK);
    }

    @PostMapping("/payment/cartao/{idCobranca}")
    public ResponseEntity<CobrancaDTO> payCobrancaCartao(@PathVariable Long idCobranca, @RequestBody CartaoDTO pagamentoDTO){
        CobrancaDTO cobrancaDTO = cobrancaService.pagarCobrancaCredito(idCobranca, pagamentoDTO.getNumeroCartao(), pagamentoDTO.getDataExpiracao(), pagamentoDTO.getCvv());
        return new ResponseEntity<>(cobrancaDTO, HttpStatus.ACCEPTED);
    }

    @PostMapping("/payment/saldo/{idCobranca}")
    public ResponseEntity<CobrancaDTO> payCobrancaSaldo(@PathVariable Long idCobranca){
        CobrancaDTO cobrancaDTO = cobrancaService.pagarCobrancaSaldo(idCobranca);
        return new ResponseEntity<>(cobrancaDTO, HttpStatus.ACCEPTED);
    }

    @PostMapping("/cancel/{idCobranca}")
    public ResponseEntity<CobrancaDTO> cancelCobranca(@PathVariable Long idCobranca) {
        CobrancaDTO cobrancaCancelada = cobrancaService.cancelarCobranca(idCobranca);
        return new ResponseEntity<>(cobrancaCancelada, HttpStatus.OK);
    }
}
