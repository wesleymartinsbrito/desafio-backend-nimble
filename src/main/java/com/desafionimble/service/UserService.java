package com.desafionimble.service;

import com.desafionimble.model.user.User;
import com.desafionimble.model.user.UserDTO;
import com.desafionimble.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UtilsService utilsService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            UtilsService utilsService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.utilsService = utilsService;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserDTO userDTO){
        if (!UtilsService.isCpfValido(userDTO.getCpf())) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setCpf(userDTO.getCpf());
        user.setBalance(BigDecimal.ZERO);
        user.setEmail(userDTO.getEmail());
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);
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
