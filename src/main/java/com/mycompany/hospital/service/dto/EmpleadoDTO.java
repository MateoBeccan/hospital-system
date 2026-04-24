package com.mycompany.hospital.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.hospital.domain.Empleado} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmpleadoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String legajo;

    @NotNull
    private LocalDate fechaIngreso;

    private LocalDate fechaBaja;

    @NotNull
    private Boolean activo;

    @NotNull
    private PersonaDTO persona;

    @NotNull
    private TipoEmpleadoDTO tipoEmpleado;

    @NotNull
    private EstadoLaboralDTO estadoLaboral;

    @NotNull
    private CargoDTO cargo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public PersonaDTO getPersona() {
        return persona;
    }

    public void setPersona(PersonaDTO persona) {
        this.persona = persona;
    }

    public TipoEmpleadoDTO getTipoEmpleado() {
        return tipoEmpleado;
    }

    public void setTipoEmpleado(TipoEmpleadoDTO tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    public EstadoLaboralDTO getEstadoLaboral() {
        return estadoLaboral;
    }

    public void setEstadoLaboral(EstadoLaboralDTO estadoLaboral) {
        this.estadoLaboral = estadoLaboral;
    }

    public CargoDTO getCargo() {
        return cargo;
    }

    public void setCargo(CargoDTO cargo) {
        this.cargo = cargo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpleadoDTO)) {
            return false;
        }

        EmpleadoDTO empleadoDTO = (EmpleadoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, empleadoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpleadoDTO{" +
            "id=" + getId() +
            ", legajo='" + getLegajo() + "'" +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            ", activo='" + getActivo() + "'" +
            ", persona=" + getPersona() +
            ", tipoEmpleado=" + getTipoEmpleado() +
            ", estadoLaboral=" + getEstadoLaboral() +
            ", cargo=" + getCargo() +
            "}";
    }
}
