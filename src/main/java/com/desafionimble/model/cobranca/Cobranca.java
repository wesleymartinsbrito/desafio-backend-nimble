package com.desafionimble.model.cobranca;

import com.desafionimble.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cobrancas")
public class Cobranca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cpf_destiny", nullable = false)
    private String cpfDestiny;
    @PositiveOrZero(message = "O valor de cobrança deve ser maior ou igual a zero")
    @Column(name = "value", nullable = false)
    private BigDecimal value;
    @Column(name = "description", nullable = true)
    private String description;
    @ManyToOne
    @JoinColumn(name = "originador_id")
    private User originador;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusCobranca status;
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento")
    private MetodoPagamento metodoPagamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpfDestiny() {
        return cpfDestiny;
    }

    public void setCpfDestiny(String cpfDestiny) {
        this.cpfDestiny = cpfDestiny;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOriginador() {
        return originador;
    }

    public void setOriginador(User originador) {
        this.originador = originador;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public StatusCobranca getStatus() {
        return status;
    }

    public void setStatus(StatusCobranca status) {
        this.status = status;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cobranca cobranca = (Cobranca) o;
        return Objects.equals(id, cobranca.id) && Objects.equals(cpfDestiny, cobranca.cpfDestiny) && Objects.equals(originador, cobranca.originador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpfDestiny, originador);
    }

    @Override
    public String toString() {
        return "Cobranca{" +
                "id=" + id +
                ", cpfDestiny='" + cpfDestiny + '\'' +
                ", value=" + value +
                ", description='" + description + '\'' +
                ", originador=" + originador +
                ", createdDate=" + createdDate +
                '}';
    }

    public enum StatusCobranca {
        PENDENTE,
        PAGA,
        CANCELADA
    }

    public enum MetodoPagamento {
        SALDO,
        CARTAO_DE_CREDITO
    }
}
