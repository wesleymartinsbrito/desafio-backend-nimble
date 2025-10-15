package com.desafionimble.service;

import com.desafionimble.model.user.User;
import com.desafionimble.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UtilsService utilsService;

    public UserService(
            UserRepository userRepository,
            UtilsService utilsService
    ) {
        this.userRepository = userRepository;
        this.utilsService = utilsService;
    }

    public void deposito(String cpf, BigDecimal value){
        User user = userRepository.findUserByCPf(cpf).orElseThrow();

        if (utilsService.autorizacao()){
            user.setBalance(user.getBalance().add(value));
            userRepository.save(user);
        } else throw new RuntimeException("Depósito não autorizado");
    }
}
