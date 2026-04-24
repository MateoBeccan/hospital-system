package com.mycompany.hospital.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.hospital.domain.TurnoLaboral} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TurnoLaboralDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String codigo;

    @NotNull
    @Size(min = 2, max = 80)
    private String nombre;

    @NotNull
    @Size(max = 5)
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
    private String horaInicio;

    @NotNull
    @Size(max = 5)
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$")
    private String horaFin;

    @Size(max = 255)
    private String descripcion;

    @NotNull
    private Boolean activo;

    @NotNull
    private LocalDate fechaAlta;

    private LocalDate fechaBaja;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TurnoLaboralDTO)) {
            return false;
        }

        TurnoLaboralDTO turnoLaboralDTO = (TurnoLaboralDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, turnoLaboralDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TurnoLaboralDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", horaFin='" + getHoraFin() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", activo='" + getActivo() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            "}";
    }
}
