package com.mycompany.hospital.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.hospital.domain.Enfermero} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnfermeroDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 40)
    private String matricula;

    private LocalDate fechaMatriculacion;

    @NotNull
    private Boolean activo;

    @NotNull
    private LocalDate fechaAlta;

    private LocalDate fechaBaja;

    @NotNull
    private EmpleadoDTO empleado;

    private TurnoLaboralDTO turnoLaboral;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDate getFechaMatriculacion() {
        return fechaMatriculacion;
    }

    public void setFechaMatriculacion(LocalDate fechaMatriculacion) {
        this.fechaMatriculacion = fechaMatriculacion;
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

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }

    public TurnoLaboralDTO getTurnoLaboral() {
        return turnoLaboral;
    }

    public void setTurnoLaboral(TurnoLaboralDTO turnoLaboral) {
        this.turnoLaboral = turnoLaboral;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnfermeroDTO)) {
            return false;
        }

        EnfermeroDTO enfermeroDTO = (EnfermeroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enfermeroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnfermeroDTO{" +
            "id=" + getId() +
            ", matricula='" + getMatricula() + "'" +
            ", fechaMatriculacion='" + getFechaMatriculacion() + "'" +
            ", activo='" + getActivo() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            ", empleado=" + getEmpleado() +
            ", turnoLaboral=" + getTurnoLaboral() +
            "}";
    }
}
