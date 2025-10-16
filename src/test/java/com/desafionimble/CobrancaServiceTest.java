package com.desafionimble;

import com.desafionimble.model.cobranca.Cobranca;
import com.desafionimble.model.user.User;
import com.desafionimble.repository.CobrancaRepository;
import com.desafionimble.repository.UserRepository;
import com.desafionimble.service.CobrancaService;
import com.desafionimble.service.UtilsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CobrancaServiceTest {
    @Mock
    private CobrancaRepository cobrancaRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UtilsService utilsService;
    @InjectMocks
    private CobrancaService cobrancaService;

    private User pagador;
    private User recebedor;
    private Cobranca cobranca;

    @BeforeEach
    void setUp() {
        pagador = new User();
        pagador.setId(1L);
        pagador.setCpf("11122233344");
        pagador.setBalance(new BigDecimal("200.00"));

        recebedor = new User();
        recebedor.setId(2L);
        recebedor.setCpf("55566677788");
        recebedor.setBalance(new BigDecimal("500.00"));

        cobranca = new Cobranca();
        cobranca.setId(10L);
        cobranca.setValue(new BigDecimal("150.00"));
        cobranca.setCpfDestiny(pagador.getCpf());
        cobranca.setOriginador(recebedor);
    }

    @Test
    void deveCancelarCobranca_QuandoStatusForPendente() {
        cobranca.setStatus(Cobranca.StatusCobranca.PENDENTE);
        when(cobrancaRepository.findById(10L)).thenReturn(Optional.of(cobranca));

        cobrancaService.cancelarCobranca(10L);

        verify(cobrancaRepository, times(1)).save(cobranca);
        assertThat(cobranca.getStatus()).isEqualTo(Cobranca.StatusCobranca.CANCELADA);
    }

    @Test
    void deveEstornarValor_QuandoCancelaCobrancaPagaComSaldo() {
        cobranca.setStatus(Cobranca.StatusCobranca.PAGA);
        cobranca.setMetodoPagamento(Cobranca.MetodoPagamento.SALDO);

        when(cobrancaRepository.findById(10L)).thenReturn(Optional.of(cobranca));
        when(userRepository.findUserByCpf(pagador.getCpf())).thenReturn(Optional.of(pagador));

        cobrancaService.cancelarCobranca(10L);

        assertThat(cobranca.getStatus()).isEqualTo(Cobranca.StatusCobranca.CANCELADA);
        assertThat(pagador.getBalance()).isEqualTo(new BigDecimal("350.00"));
        assertThat(recebedor.getBalance()).isEqualTo(new BigDecimal("350.00"));
        verify(userRepository, times(2)).save(any(User.class));
        verify(cobrancaRepository, times(1)).save(cobranca);
    }

    @Test
    void deveCancelarCobranca_QuandoPagaComCartaoEAutorizacaoConcedida() {
        cobranca.setStatus(Cobranca.StatusCobranca.PAGA);
        cobranca.setMetodoPagamento(Cobranca.MetodoPagamento.CARTAO_DE_CREDITO);

        when(cobrancaRepository.findById(10L)).thenReturn(Optional.of(cobranca));
        when(utilsService.autorizacao()).thenReturn(true);

        cobrancaService.cancelarCobranca(10L);

        assertThat(cobranca.getStatus()).isEqualTo(Cobranca.StatusCobranca.CANCELADA);
        verify(cobrancaRepository, times(1)).save(cobranca);
    }

    @Test
    void deveLancarExcecaoAoCancelarComCartao_QuandoAutorizacaoForNegada() {
        cobranca.setStatus(Cobranca.StatusCobranca.PAGA);
        cobranca.setMetodoPagamento(Cobranca.MetodoPagamento.CARTAO_DE_CREDITO);

        when(cobrancaRepository.findById(10L)).thenReturn(Optional.of(cobranca));
        when(utilsService.autorizacao()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            cobrancaService.cancelarCobranca(10L);
        });

        assertThat(cobranca.getStatus()).isEqualTo(Cobranca.StatusCobranca.PAGA);
    }

    @Test
    void deveLancarExcecao_QuandoTentaCancelarCobrancaJaCancelada() {
        cobranca.setStatus(Cobranca.StatusCobranca.CANCELADA);
        when(cobrancaRepository.findById(10L)).thenReturn(Optional.of(cobranca));

        assertThrows(IllegalStateException.class, () -> {
            cobrancaService.cancelarCobranca(10L);
        });
    }
}
