package com.desafionimble.model.cobranca;

import com.desafionimble.model.user.User;
import jakarta.persistence.*;

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
    @Column(name = "value", nullable = false)
    private BigDecimal value;
    @Column(name = "description", nullable = true)
    private String description;
    @ManyToOne
    @JoinColumn(name = "devedor_id")
    private User devedor;
    @Column(name = "created_date")
    private LocalDateTime createdDate;

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

    public User getDevedor() {
        return devedor;
    }

    public void setDevedor(User devedor) {
        this.devedor = devedor;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cobranca cobranca = (Cobranca) o;
        return Objects.equals(id, cobranca.id) && Objects.equals(cpfDestiny, cobranca.cpfDestiny) && Objects.equals(devedor, cobranca.devedor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpfDestiny, devedor);
    }

    @Override
    public String toString() {
        return "Cobranca{" +
                "id=" + id +
                ", cpfDestiny='" + cpfDestiny + '\'' +
                ", value=" + value +
                ", description='" + description + '\'' +
                ", devedor=" + devedor +
                ", createdDate=" + createdDate +
                '}';
    }
}
