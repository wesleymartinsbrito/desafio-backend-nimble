package com.desafionimble.resource;

import com.desafionimble.model.CartaoDTO;
import com.desafionimble.model.cobranca.CobrancaDTO;
import com.desafionimble.service.CobrancaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cobrancas")
@Tag(name = "Cobranças", description = "Endpoints para gerenciamento de cobranças")
public class CobrancaResource {

    private final CobrancaService cobrancaService;

    public CobrancaResource(
            CobrancaService cobrancaService
    ) {
        this.cobrancaService = cobrancaService;
    }

    @Operation(summary = "Criar uma nova cobrança", description = "Cria uma cobrança de um usuário originador para um destinatário via CPF.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cobrança criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CobrancaDTO> createCobranca(@RequestBody CobrancaDTO cobrancaDTO){
        CobrancaDTO cobranca = cobrancaService.createCobranca(cobrancaDTO);
        return new ResponseEntity<>(cobranca, HttpStatus.CREATED);
    }

    @Operation(summary = "Consultar cobranças recebidas", description = "Busca uma lista de cobranças que um usuário recebeu, filtrada por status.")
    @GetMapping("/receive")
    public ResponseEntity<List<CobrancaDTO>> findCobrancaReceive(@Parameter(description = "CPF do usuário destinatário da cobrança", required = true, example = "12345678900") @RequestParam String cpf,
                                                                 @Parameter(description = "Status da cobrança (1=Pendente, 2=Paga, 3=Cancelada)", required = true, example = "1") @RequestParam int status){
        List<CobrancaDTO> cobrancaDTOList = cobrancaService.findReceive(cpf, status);
        return new ResponseEntity<>(cobrancaDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Consultar cobranças enviadas", description = "Busca uma lista de cobranças que um usuário enviou, filtrada por status.")
    @GetMapping("/send")
    public ResponseEntity<List<CobrancaDTO>> findCobrancaSend(@Parameter(description = "CPF do usuário originador da cobrança", required = true, example = "12345678900") @RequestParam String cpf,
                                                              @Parameter(description = "Status da cobrança (1=Pendente, 2=Paga, 3=Cancelada)", required = true, example = "1") @RequestParam int status){
        List<CobrancaDTO> cobrancaDTOList = cobrancaService.findSend(cpf, status);
        return new ResponseEntity<>(cobrancaDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Pagar cobrança com cartão de crédito", description = "Efetua o pagamento de uma cobrança utilizando cartão de crédito do usuário pagador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Pagamento aceito e processado"),
            @ApiResponse(responseCode = "400", description = "Pagamento negado pelo autorizador"),
            @ApiResponse(responseCode = "404", description = "Cobrança não encontrada", content = @Content)
    })
    @PostMapping("/payment/cartao/{idCobranca}")
    public ResponseEntity<CobrancaDTO> payCobrancaCartao(@PathVariable Long idCobranca, @RequestBody CartaoDTO pagamentoDTO){
        CobrancaDTO cobrancaDTO = cobrancaService.pagarCobrancaCredito(idCobranca, pagamentoDTO.getNumeroCartao(), pagamentoDTO.getDataExpiracao(), pagamentoDTO.getCvv());
        return new ResponseEntity<>(cobrancaDTO, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Pagar cobrança com saldo", description = "Efetua o pagamento de uma cobrança utilizando o saldo em conta do usuário pagador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Pagamento aceito e processado"),
            @ApiResponse(responseCode = "404", description = "Cobrança não encontrada", content = @Content)
    })
    @PostMapping("/payment/saldo/{idCobranca}")
    public ResponseEntity<CobrancaDTO> payCobrancaSaldo(@Parameter(description = "ID da cobrança a ser paga", required = true, example = "1") @PathVariable Long idCobranca){
        CobrancaDTO cobrancaDTO = cobrancaService.pagarCobrancaSaldo(idCobranca);
        return new ResponseEntity<>(cobrancaDTO, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Cancelar cobranca", description = "Efetua o cancelamento de uma cobrança.")
    @PostMapping("/cancel/{idCobranca}")
    public ResponseEntity<CobrancaDTO> cancelCobranca(@Parameter(description = "ID da cobrança a ser cancelada", required = true, example = "1") @PathVariable Long idCobranca) {
        CobrancaDTO cobrancaCancelada = cobrancaService.cancelarCobranca(idCobranca);
        return new ResponseEntity<>(cobrancaCancelada, HttpStatus.OK);
    }
}
