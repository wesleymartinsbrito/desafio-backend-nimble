package com.desafionimble.repository;

import com.desafionimble.model.cobranca.Cobranca;
import com.desafionimble.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CobrancaRepository extends JpaRepository<Cobranca, Long> {
    List<Cobranca> findAllByStatusAndCpfDestiny(String status, String cpfDestiny);
    List<Cobranca> findAllByStatusAndOriginador(String status, User originador);
    List<Cobranca> findAllByCpfDestiny(String cpfDestiny);
    List<Cobranca> findAllByOriginador(User originador);
}
