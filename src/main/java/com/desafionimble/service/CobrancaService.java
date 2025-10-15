package com.desafionimble.service;

import com.desafionimble.model.cobranca.Cobranca;
import com.desafionimble.model.cobranca.CobrancaDTO;
import com.desafionimble.model.user.User;
import com.desafionimble.repository.CobrancaRepository;
import com.desafionimble.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final UserRepository userRepository;

    public CobrancaService(
            CobrancaRepository cobrancaRepository,
            UserRepository userRepository
    ){
        this.cobrancaRepository = cobrancaRepository;
        this.userRepository = userRepository;
    }

    public CobrancaDTO createCobranca(CobrancaDTO cobrancaDTO){
        Optional<User> originador = userRepository.findById(cobrancaDTO.getIdOriginador());
        Cobranca cobrancaVO = toEntity(cobrancaDTO, originador.orElseThrow(), "PENDENTE");
        cobrancaVO.setCreatedDate(LocalDateTime.now());
        Cobranca cobrancaSaved = cobrancaRepository.save(cobrancaVO);
        cobrancaDTO.setId(cobrancaSaved.getId());
        return cobrancaDTO;
    }

    public List<CobrancaDTO> findReceive(String cpf, int status){
        Optional<User> user = userRepository.findUserByCPf(cpf);
        if(status == 1) {
            List<Cobranca> cobrancas = cobrancaRepository.findAllByStatusAndCpfDestiny("PENDENTE", cpf);
            List<CobrancaDTO> cobrancasDTO = new ArrayList<>();
            for (int i = 0; i <= cobrancas.size(); i++) {
                Cobranca cobranca = cobrancas.get(i);
                cobrancasDTO.add(toDto(cobranca));
            }
            return cobrancasDTO;
        } else if (status == 2) {
            List<Cobranca> cobrancas = cobrancaRepository.findAllByStatusAndCpfDestiny("PAGO", cpf);
            List<CobrancaDTO> cobrancasDTO = new ArrayList<>();
            for (int i = 0; i <= cobrancas.size(); i++) {
                Cobranca cobranca = cobrancas.get(i);
                cobrancasDTO.add(toDto(cobranca));
            }
            return cobrancasDTO;
        } else if (status == 3) {
            List<Cobranca> cobrancas = cobrancaRepository.findAllByStatusAndCpfDestiny("CANCELADO", cpf);
            List<CobrancaDTO> cobrancasDTO = new ArrayList<>();
            for (int i = 0; i <= cobrancas.size(); i++) {
                Cobranca cobranca = cobrancas.get(i);
                cobrancasDTO.add(toDto(cobranca));
            }
            return cobrancasDTO;
        }
        return new ArrayList<CobrancaDTO>();
    }

    public List<CobrancaDTO> findSend(String cpf, int status){
        Optional<User> user = userRepository.findUserByCPf(cpf);
        if(status == 1) {
            List<Cobranca> cobrancas = cobrancaRepository.findAllByStatusAndDevedor("PENDENTE", user.orElseThrow());
            List<CobrancaDTO> cobrancasDTO = new ArrayList<>();
            for (int i = 0; i <= cobrancas.size(); i++) {
                Cobranca cobranca = cobrancas.get(i);
                cobrancasDTO.add(toDto(cobranca));
            }
            return cobrancasDTO;
        } else if (status == 2) {
            List<Cobranca> cobrancas = cobrancaRepository.findAllByStatusAndDevedor("PAGO", user.orElseThrow());
            List<CobrancaDTO> cobrancasDTO = new ArrayList<>();
            for (int i = 0; i <= cobrancas.size(); i++) {
                Cobranca cobranca = cobrancas.get(i);
                cobrancasDTO.add(toDto(cobranca));
            }
            return cobrancasDTO;
        } else if (status == 3) {
            List<Cobranca> cobrancas = cobrancaRepository.findAllByStatusAndDevedor("CANCELADO", user.orElseThrow());
            List<CobrancaDTO> cobrancasDTO = new ArrayList<>();
            for (int i = 0; i <= cobrancas.size(); i++) {
                Cobranca cobranca = cobrancas.get(i);
                cobrancasDTO.add(toDto(cobranca));
            }
            return cobrancasDTO;
        }
        return new ArrayList<CobrancaDTO>();
    }

    private CobrancaDTO toDto(Cobranca cobranca){
        CobrancaDTO cobrancaDTO = new CobrancaDTO();
        cobrancaDTO.setId(cobranca.getId());
        cobrancaDTO.setIdOriginador(cobranca.getDevedor().getId());
        cobrancaDTO.setValue(cobranca.getValue());
        cobrancaDTO.setDescription(cobranca.getDescription());
        cobrancaDTO.setCpfDestiny(cobranca.getCpfDestiny());
        return cobrancaDTO;
    }

    private Cobranca toEntity(CobrancaDTO cobrancaDTO, User user, String status){
        Cobranca cobranca = new Cobranca();
        cobranca.setId(cobrancaDTO.getId());
        cobranca.setDevedor(user);
        cobranca.setValue(cobrancaDTO.getValue());
        cobranca.setDescription(cobrancaDTO.getDescription());
        cobranca.setCpfDestiny(cobrancaDTO.getCpfDestiny());
        cobranca.setStatus(status);
        return cobranca;
    }
}
