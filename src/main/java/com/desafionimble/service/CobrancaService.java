package com.desafionimble.service;

import com.desafionimble.model.cobranca.Cobranca;
import com.desafionimble.model.cobranca.CobrancaDTO;
import com.desafionimble.model.user.User;
import com.desafionimble.repository.CobrancaRepository;
import com.desafionimble.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final UserRepository userRepository;
    private final UtilsService utilsService;


    public CobrancaService(
            CobrancaRepository cobrancaRepository,
            UserRepository userRepository,
            UtilsService utilsService
    ){
        this.cobrancaRepository = cobrancaRepository;
        this.userRepository = userRepository;
        this.utilsService = utilsService;
    }

    public CobrancaDTO createCobranca(CobrancaDTO cobrancaDTO){
        Optional<User> originador = userRepository.findById(cobrancaDTO.getIdOriginador());
        Cobranca cobrancaVO = toEntity(cobrancaDTO, originador.orElseThrow(), "PENDENTE");
        cobrancaVO.setCreatedDate(LocalDateTime.now());
        Cobranca cobrancaSaved = cobrancaRepository.save(cobrancaVO);
        cobrancaDTO.setId(cobrancaSaved.getId());
        return cobrancaDTO;
    }

    public List<CobrancaDTO> findReceive(String cpf, int statusId){
        Cobranca.StatusCobranca statusEnum = getStatusEnumById(statusId);
        List<Cobranca> cobrancas = cobrancaRepository.findAllByStatusAndCpfDestiny(statusEnum, cpf);

        return cobrancas.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<CobrancaDTO> findSend(String cpf, int statusId){
        Optional<User> user = userRepository.findUserByCpf(cpf);
        Cobranca.StatusCobranca statusEnum = getStatusEnumById(statusId);
        List<Cobranca> cobrancas = cobrancaRepository.findAllByStatusAndOriginador(statusEnum, user.orElseThrow());

        return cobrancas.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CobrancaDTO pagarCobrancaSaldo(Long idCobranca){
        Cobranca cobranca = cobrancaRepository.findById(idCobranca).orElseThrow();
        User usuarioDevedor = userRepository.findUserByCpf(cobranca.getCpfDestiny()).orElseThrow();
        User usuarioRecebedor = cobranca.getOriginador();
            if (usuarioDevedor.getBalance().compareTo(cobranca.getValue()) >= 0) {
                cobranca.setStatus(Cobranca.StatusCobranca.PAGA);
                cobranca.setMetodoPagamento(Cobranca.MetodoPagamento.SALDO);
                usuarioDevedor.setBalance(usuarioDevedor.getBalance().subtract(cobranca.getValue()));
                usuarioRecebedor.setBalance(usuarioRecebedor.getBalance().add(cobranca.getValue()));
                cobrancaRepository.save(cobranca);
                userRepository.save(usuarioDevedor);
                userRepository.save(usuarioRecebedor);
                return toDto(cobranca);
            } else {
                throw new RuntimeException("Saldo do usuário devedor é menor que o saldo devedor.");
            }
    }

    @Transactional
    public CobrancaDTO pagarCobrancaCredito(Long idCobranca, Long numeroCartao, LocalDate dataExpiracao, int cvv){
        Cobranca cobranca = cobrancaRepository.findById(idCobranca).orElseThrow();
        User usuarioRecebedor = cobranca.getOriginador();

        if (dataExpiracao.isBefore(LocalDate.now())) throw new RuntimeException("Data de expiração anterior a data atual.");

        if(utilsService.autorizacao()) {
            cobranca.setStatus(Cobranca.StatusCobranca.PAGA);
            cobranca.setMetodoPagamento(Cobranca.MetodoPagamento.CARTAO_DE_CREDITO);
            usuarioRecebedor.setBalance(usuarioRecebedor.getBalance().add(cobranca.getValue()));
            cobrancaRepository.save(cobranca);
            userRepository.save(usuarioRecebedor);
            return toDto(cobranca);
        } else {
            throw new RuntimeException("Pagamento do saldo devedor com o cartão de crédito não autorizado.");
        }
    }

    @Transactional
    public CobrancaDTO cancelarCobranca(Long idCobranca) {
        Cobranca cobranca = cobrancaRepository.findById(idCobranca)
                .orElseThrow(() -> new RuntimeException("Cobrança não encontrada."));

        switch (cobranca.getStatus()) {
            case PENDENTE:
                return cancelarCobrancaPendente(cobranca);

            case PAGA:
                if (cobranca.getMetodoPagamento() == Cobranca.MetodoPagamento.SALDO) {
                    return cancelarCobrancaPagaComSaldo(cobranca);
                } else if (cobranca.getMetodoPagamento() == Cobranca.MetodoPagamento.CARTAO_DE_CREDITO) {
                    return cancelarCobrancaPagaComCartao(cobranca);
                } else {
                    throw new IllegalStateException("Cobrança paga sem método de pagamento definido.");
                }

            case CANCELADA:
                throw new IllegalStateException("Esta cobrança já está cancelada.");

            default:
                throw new IllegalStateException("Status da cobrança desconhecido.");
        }
    }

    private CobrancaDTO cancelarCobrancaPendente(Cobranca cobranca) {
        cobranca.setStatus(Cobranca.StatusCobranca.CANCELADA);
        cobrancaRepository.save(cobranca);
        return toDto(cobranca);
    }

    private CobrancaDTO cancelarCobrancaPagaComSaldo(Cobranca cobranca) {
        User pagador = userRepository.findUserByCpf(cobranca.getCpfDestiny())
                .orElseThrow(() -> new RuntimeException("Usuário pagador não encontrado."));
        User recebedor = cobranca.getOriginador();
        BigDecimal valor = cobranca.getValue();

        pagador.setBalance(pagador.getBalance().add(valor));
        recebedor.setBalance(recebedor.getBalance().subtract(valor));

        cobranca.setStatus(Cobranca.StatusCobranca.CANCELADA);

        userRepository.save(pagador);
        userRepository.save(recebedor);
        cobrancaRepository.save(cobranca);

        return toDto(cobranca);
    }

    private CobrancaDTO cancelarCobrancaPagaComCartao(Cobranca cobranca) {
        if (utilsService.autorizacao()) {
            cobranca.setStatus(Cobranca.StatusCobranca.CANCELADA);
            cobrancaRepository.save(cobranca);
            return toDto(cobranca);
        } else {
            throw new RuntimeException("Cancelamento não autorizado pelo serviço externo.");
        }
    }

    private CobrancaDTO toDto(Cobranca cobranca){
        CobrancaDTO cobrancaDTO = new CobrancaDTO();
        cobrancaDTO.setId(cobranca.getId());
        cobrancaDTO.setIdOriginador(cobranca.getOriginador().getId());
        cobrancaDTO.setValue(cobranca.getValue());
        cobrancaDTO.setDescription(cobranca.getDescription());
        cobrancaDTO.setCpfDestiny(cobranca.getCpfDestiny());
        return cobrancaDTO;
    }

    private Cobranca toEntity(CobrancaDTO cobrancaDTO, User user, String status){
        Cobranca cobranca = new Cobranca();
        cobranca.setId(cobrancaDTO.getId());
        cobranca.setOriginador(user);
        cobranca.setValue(cobrancaDTO.getValue());
        cobranca.setDescription(cobrancaDTO.getDescription());
        cobranca.setCpfDestiny(cobrancaDTO.getCpfDestiny());
        cobranca.setStatus(Cobranca.StatusCobranca.valueOf(status));
        return cobranca;
    }

    private Cobranca.StatusCobranca getStatusEnumById(int statusId) {
        return switch (statusId) {
            case 1 -> Cobranca.StatusCobranca.PENDENTE;
            case 2 -> Cobranca.StatusCobranca.PAGA;
            case 3 -> Cobranca.StatusCobranca.CANCELADA;
            default -> throw new IllegalArgumentException("Status ID inválido: " + statusId);
        };
    }
}
