package com.mycompany.hospital.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.hospital.domain.Medico} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 40)
    private String matricula;

    private LocalDate fechaMatriculacion;

    @Size(max = 255)
    private String firmaDigital;

    @NotNull
    private Boolean atiendeConsultorio;

    @NotNull
    private Boolean activo;

    @NotNull
    private LocalDate fechaAlta;

    private LocalDate fechaBaja;

    @NotNull
    private EmpleadoDTO empleado;

    @NotNull
    private EspecialidadDTO especialidad;

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

    public String getFirmaDigital() {
        return firmaDigital;
    }

    public void setFirmaDigital(String firmaDigital) {
        this.firmaDigital = firmaDigital;
    }

    public Boolean getAtiendeConsultorio() {
        return atiendeConsultorio;
    }

    public void setAtiendeConsultorio(Boolean atiendeConsultorio) {
        this.atiendeConsultorio = atiendeConsultorio;
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

    public EspecialidadDTO getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(EspecialidadDTO especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicoDTO)) {
            return false;
        }

        MedicoDTO medicoDTO = (MedicoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicoDTO{" +
            "id=" + getId() +
            ", matricula='" + getMatricula() + "'" +
            ", fechaMatriculacion='" + getFechaMatriculacion() + "'" +
            ", firmaDigital='" + getFirmaDigital() + "'" +
            ", atiendeConsultorio='" + getAtiendeConsultorio() + "'" +
            ", activo='" + getActivo() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            ", empleado=" + getEmpleado() +
            ", especialidad=" + getEspecialidad() +
            "}";
    }
}
