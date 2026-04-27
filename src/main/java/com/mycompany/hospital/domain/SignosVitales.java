package com.mycompany.hospital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A SignosVitales.
 */
@Entity
@Table(name = "signos_vitales")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SignosVitales implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_hora_registro", nullable = false)
    private Instant fechaHoraRegistro;

    @DecimalMin(value = "0")
    @Column(name = "peso", precision = 21, scale = 2)
    private BigDecimal peso;

    @DecimalMin(value = "0")
    @Column(name = "talla", precision = 21, scale = 2)
    private BigDecimal talla;

    @DecimalMin(value = "0")
    @Column(name = "temperatura", precision = 21, scale = 2)
    private BigDecimal temperatura;

    @Size(max = 30)
    @Column(name = "presion_arterial", length = 30)
    private String presionArterial;

    @Min(value = 0)
    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Min(value = 0)
    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "saturacion_oxigeno")
    private Integer saturacionOxigeno;

    @Lob
    @Column(name = "observaciones")
    private String observaciones;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @NotNull
    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "turno", "paciente", "medico", "historiaClinica" }, allowSetters = true)
    private Consulta consulta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SignosVitales id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaHoraRegistro() {
        return this.fechaHoraRegistro;
    }

    public SignosVitales fechaHoraRegistro(Instant fechaHoraRegistro) {
        this.setFechaHoraRegistro(fechaHoraRegistro);
        return this;
    }

    public void setFechaHoraRegistro(Instant fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public BigDecimal getPeso() {
        return this.peso;
    }

    public SignosVitales peso(BigDecimal peso) {
        this.setPeso(peso);
        return this;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getTalla() {
        return this.talla;
    }

    public SignosVitales talla(BigDecimal talla) {
        this.setTalla(talla);
        return this;
    }

    public void setTalla(BigDecimal talla) {
        this.talla = talla;
    }

    public BigDecimal getTemperatura() {
        return this.temperatura;
    }

    public SignosVitales temperatura(BigDecimal temperatura) {
        this.setTemperatura(temperatura);
        return this;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura;
    }

    public String getPresionArterial() {
        return this.presionArterial;
    }

    public SignosVitales presionArterial(String presionArterial) {
        this.setPresionArterial(presionArterial);
        return this;
    }

    public void setPresionArterial(String presionArterial) {
        this.presionArterial = presionArterial;
    }

    public Integer getFrecuenciaCardiaca() {
        return this.frecuenciaCardiaca;
    }

    public SignosVitales frecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.setFrecuenciaCardiaca(frecuenciaCardiaca);
        return this;
    }

    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public Integer getFrecuenciaRespiratoria() {
        return this.frecuenciaRespiratoria;
    }

    public SignosVitales frecuenciaRespiratoria(Integer frecuenciaRespiratoria) {
        this.setFrecuenciaRespiratoria(frecuenciaRespiratoria);
        return this;
    }

    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) {
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    }

    public Integer getSaturacionOxigeno() {
        return this.saturacionOxigeno;
    }

    public SignosVitales saturacionOxigeno(Integer saturacionOxigeno) {
        this.setSaturacionOxigeno(saturacionOxigeno);
        return this;
    }

    public void setSaturacionOxigeno(Integer saturacionOxigeno) {
        this.saturacionOxigeno = saturacionOxigeno;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public SignosVitales observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public SignosVitales activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaAlta() {
        return this.fechaAlta;
    }

    public SignosVitales fechaAlta(LocalDate fechaAlta) {
        this.setFechaAlta(fechaAlta);
        return this;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return this.fechaBaja;
    }

    public SignosVitales fechaBaja(LocalDate fechaBaja) {
        this.setFechaBaja(fechaBaja);
        return this;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Consulta getConsulta() {
        return this.consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public SignosVitales consulta(Consulta consulta) {
        this.setConsulta(consulta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignosVitales)) {
            return false;
        }
        return getId() != null && getId().equals(((SignosVitales) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignosVitales{" +
            "id=" + getId() +
            ", fechaHoraRegistro='" + getFechaHoraRegistro() + "'" +
            ", peso=" + getPeso() +
            ", talla=" + getTalla() +
            ", temperatura=" + getTemperatura() +
            ", presionArterial='" + getPresionArterial() + "'" +
            ", frecuenciaCardiaca=" + getFrecuenciaCardiaca() +
            ", frecuenciaRespiratoria=" + getFrecuenciaRespiratoria() +
            ", saturacionOxigeno=" + getSaturacionOxigeno() +
            ", observaciones='" + getObservaciones() + "'" +
            ", activo='" + getActivo() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            "}";
    }
}
