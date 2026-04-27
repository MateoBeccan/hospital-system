package com.mycompany.hospital.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.hospital.domain.Consulta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsultaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 40)
    private String codigo;

    @NotNull
    private Instant fechaHoraInicio;

    private Instant fechaHoraFin;

    @Lob
    private String sintomas;

    @NotNull
    @Size(min = 3, max = 255)
    private String motivoConsulta;

    @Lob
    private String examenFisico;

    @Lob
    private String observaciones;

    @Lob
    private String indicaciones;

    @NotNull
    private Boolean activa;

    @NotNull
    private LocalDate fechaAlta;

    private LocalDate fechaBaja;

    private TurnoDTO turno;

    @NotNull
    private PacienteDTO paciente;

    @NotNull
    private MedicoDTO medico;

    @NotNull
    private HistoriaClinicaDTO historiaClinica;

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

    public Instant getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Instant fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Instant getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(Instant fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getExamenFisico() {
        return examenFisico;
    }

    public void setExamenFisico(String examenFisico) {
        this.examenFisico = examenFisico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
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

    public TurnoDTO getTurno() {
        return turno;
    }

    public void setTurno(TurnoDTO turno) {
        this.turno = turno;
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

    public HistoriaClinicaDTO getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinicaDTO historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsultaDTO)) {
            return false;
        }

        ConsultaDTO consultaDTO = (ConsultaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consultaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsultaDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", fechaHoraInicio='" + getFechaHoraInicio() + "'" +
            ", fechaHoraFin='" + getFechaHoraFin() + "'" +
            ", sintomas='" + getSintomas() + "'" +
            ", motivoConsulta='" + getMotivoConsulta() + "'" +
            ", examenFisico='" + getExamenFisico() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", indicaciones='" + getIndicaciones() + "'" +
            ", activa='" + getActiva() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            ", turno=" + getTurno() +
            ", paciente=" + getPaciente() +
            ", medico=" + getMedico() +
            ", historiaClinica=" + getHistoriaClinica() +
            "}";
    }
}
