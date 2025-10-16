package com.desafionimble;

import com.desafionimble.model.user.User;
import com.desafionimble.model.user.UserDTO;
import com.desafionimble.repository.UserRepository;
import com.desafionimble.service.UserService;
import com.desafionimble.service.UtilsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.ValidationUtils;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UtilsService utilsService;

    @InjectMocks
    private UserService userService;

    @Test
    void deveCriarUsuarioComSucesso_QuandoDadosForemValidos() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Nome Teste");
        userDTO.setEmail("teste@email.com");
        userDTO.setPassword("123456");
        userDTO.setCpf("52953256012");
        when(passwordEncoder.encode("123456")).thenReturn("senha_hasheada");

        try (MockedStatic<UtilsService> mockedValidation = Mockito.mockStatic(UtilsService.class)) {
            mockedValidation.when(() -> UtilsService.isCpfValido("52953256012")).thenReturn(true);

            userService.createUser(userDTO);

            verify(passwordEncoder, times(1)).encode("123456");
            verify(userRepository, times(1)).save(any(User.class));
        }
    }

    @Test
    void deveLancarExcecao_QuandoCpfForInvalido() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Nome Teste");
        userDTO.setEmail("teste@email.com");
        userDTO.setPassword("123456");
        userDTO.setCpf("11111111111");
        try (MockedStatic<UtilsService> mockedValidation = Mockito.mockStatic(UtilsService.class)) {
            mockedValidation.when(() -> UtilsService.isCpfValido("11111111111")).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () -> {
                userService.createUser(userDTO);
            });

            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void deveRealizarDepositoComSucesso_QuandoUsuarioExisteEAutorizacaoConcedida() {
        String cpf = "52953256012";
        BigDecimal valorDeposito = new BigDecimal("100.00");
        User user = new User();
        user.setCpf(cpf);
        user.setBalance(new BigDecimal("50.00"));

        when(userRepository.findUserByCpf(cpf)).thenReturn(Optional.of(user));
        when(utilsService.autorizacao()).thenReturn(true);

        userService.deposito(cpf, valorDeposito);

        verify(userRepository, times(1)).save(any(User.class));
        assertThat(user.getBalance()).isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    void deveLancarExcecao_QuandoUsuarioNaoExiste() {
        String cpf = "00000000000";
        when(userRepository.findUserByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            userService.deposito(cpf, new BigDecimal("100"));
        });
    }

    @Test
    void deveLancarExcecao_QuandoAutorizacaoForNegada() {
        String cpf = "52953256012";
        User user = new User();
        user.setCpf(cpf);
        user.setBalance(new BigDecimal("50.00"));

        when(userRepository.findUserByCpf(cpf)).thenReturn(Optional.of(user));
        when(utilsService.autorizacao()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            userService.deposito(cpf, new BigDecimal("100"));
        });

        assertThat(user.getBalance()).isEqualTo(new BigDecimal("50.00"));
    }
}
