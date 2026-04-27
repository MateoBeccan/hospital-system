package com.mycompany.hospital.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.hospital.domain.Diagnostico} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DiagnosticoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 40)
    private String codigo;

    @NotNull
    private LocalDate fechaDiagnostico;

    @NotNull
    @Size(min = 3, max = 255)
    private String descripcion;

    @Lob
    private String observaciones;

    @NotNull
    private Boolean activo;

    private LocalDate fechaResolucion;

    @NotNull
    private Boolean esPrincipal;

    @NotNull
    private LocalDate fechaAlta;

    private LocalDate fechaBaja;

    @NotNull
    private ConsultaDTO consulta;

    @NotNull
    private PacienteDTO paciente;

    @NotNull
    private MedicoDTO medico;

    @NotNull
    private TipoDiagnosticoDTO tipoDiagnostico;

    @NotNull
    private EstadoDiagnosticoDTO estadoDiagnostico;

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

    public LocalDate getFechaDiagnostico() {
        return fechaDiagnostico;
    }

    public void setFechaDiagnostico(LocalDate fechaDiagnostico) {
        this.fechaDiagnostico = fechaDiagnostico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public LocalDate getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDate fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public Boolean getEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(Boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
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

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    public MedicoDTO getMedico() {
        return medico;
    }

    public void setMedico(MedicoDTO medico) {
        this.medico = medico;
    }

    public TipoDiagnosticoDTO getTipoDiagnostico() {
        return tipoDiagnostico;
    }

    public void setTipoDiagnostico(TipoDiagnosticoDTO tipoDiagnostico) {
        this.tipoDiagnostico = tipoDiagnostico;
    }

    public EstadoDiagnosticoDTO getEstadoDiagnostico() {
        return estadoDiagnostico;
    }

    public void setEstadoDiagnostico(EstadoDiagnosticoDTO estadoDiagnostico) {
        this.estadoDiagnostico = estadoDiagnostico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiagnosticoDTO)) {
            return false;
        }

        DiagnosticoDTO diagnosticoDTO = (DiagnosticoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, diagnosticoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DiagnosticoDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", fechaDiagnostico='" + getFechaDiagnostico() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", activo='" + getActivo() + "'" +
            ", fechaResolucion='" + getFechaResolucion() + "'" +
            ", esPrincipal='" + getEsPrincipal() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            ", consulta=" + getConsulta() +
            ", paciente=" + getPaciente() +
            ", medico=" + getMedico() +
            ", tipoDiagnostico=" + getTipoDiagnostico() +
            ", estadoDiagnostico=" + getEstadoDiagnostico() +
            "}";
    }
}
