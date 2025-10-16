package com.desafionimble.repository;

import com.desafionimble.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByCpf(String cpf);
    Optional<UserDetails> findUserByCpfOrEmail(String cpf, String email);
}
