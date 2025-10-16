package com.desafionimble.model.cobranca;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Modelo de dados para criar ou visualizar uma cobrança")
public class CobrancaDTO {
    @Schema(description = "ID único da cobrança", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Schema(description = "CPF do usuário que deve pagar a cobrança (destinatário)", example = "12345678900")
    private String cpfDestiny;
    @Schema(description = "Valor da cobrança", example = "99.90")
    private BigDecimal value;
    @Schema(description = "Descrição opcional da cobrança", example = "Referente ao serviço X")
    private String description;
    @Schema(description = "ID do usuário que criou a cobrança (originador)", example = "15")
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
