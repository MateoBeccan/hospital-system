package com.mycompany.hospital.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.hospital.domain.SignosVitales} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SignosVitalesDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant fechaHoraRegistro;

    @DecimalMin(value = "0")
    private BigDecimal peso;

    @DecimalMin(value = "0")
    private BigDecimal talla;

    @DecimalMin(value = "0")
    private BigDecimal temperatura;

    @Size(max = 30)
    private String presionArterial;

    @Min(value = 0)
    private Integer frecuenciaCardiaca;

    @Min(value = 0)
    private Integer frecuenciaRespiratoria;

    @Min(value = 0)
    @Max(value = 100)
    private Integer saturacionOxigeno;

    @Lob
    private String observaciones;

    @NotNull
    private Boolean activo;

    @NotNull
    private LocalDate fechaAlta;

    private LocalDate fechaBaja;

    @NotNull
    private ConsultaDTO consulta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(Instant fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getTalla() {
        return talla;
    }

    public void setTalla(BigDecimal talla) {
        this.talla = talla;
    }

    public BigDecimal getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(BigDecimal temperatura) {
        this.temperatura = temperatura;
    }

    public String getPresionArterial() {
        return presionArterial;
    }

    public void setPresionArterial(String presionArterial) {
        this.presionArterial = presionArterial;
    }

    public Integer getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public Integer getFrecuenciaRespiratoria() {
        return frecuenciaRespiratoria;
    }

    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) {
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    }

    public Integer getSaturacionOxigeno() {
        return saturacionOxigeno;
    }

    public void setSaturacionOxigeno(Integer saturacionOxigeno) {
        this.saturacionOxigeno = saturacionOxigeno;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public ConsultaDTO getConsulta() {
        return consulta;
    }

    public void setConsulta(ConsultaDTO consulta) {
        this.consulta = consulta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SignosVitalesDTO)) {
            return false;
        }

        SignosVitalesDTO signosVitalesDTO = (SignosVitalesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, signosVitalesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignosVitalesDTO{" +
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
            ", consulta=" + getConsulta() +
            "}";
    }
}
