package com.desafionimble.service;

import com.desafionimble.model.user.User;
import com.desafionimble.model.user.UserDTO;
import com.desafionimble.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public User createUser(UserDTO userDTO){
        User user = new User();
        user.setName(userDTO.getName());
        user.setCpf(userDTO.getCpf());
        user.setBalance(userDTO.getBalance());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return userRepository.save(user);
    }

    @Transactional
    public void deposito(String cpf, BigDecimal value){
        User user = userRepository.findUserByCpf(cpf).orElseThrow();

        if (utilsService.autorizacao()){
            user.setBalance(user.getBalance().add(value));
            userRepository.save(user);
        } else throw new RuntimeException("Depósito não autorizado");
    }
}
