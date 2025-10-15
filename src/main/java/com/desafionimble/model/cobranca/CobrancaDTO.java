package com.desafionimble.model.cobranca;

import java.math.BigDecimal;

public class CobrancaDTO {
    private Long id;
    private String cpfDestiny;
    private BigDecimal value;
    private String description;
    private Long idOriginador;

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

    public Long getIdOriginador() {
        return idOriginador;
    }

    public void setIdOriginador(Long idOriginador) {
        this.idOriginador = idOriginador;
    }
}
