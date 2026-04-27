package com.mycompany.hospital.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.hospital.domain.Turno} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TurnoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 40)
    private String codigo;

    @NotNull
    private Instant fechaHora;

    @NotNull
    @Min(value = 5)
    @Max(value = 240)
    private Integer duracionMinutos;

    @NotNull
    @Size(min = 3, max = 255)
    private String motivoConsulta;

    @Lob
    private String observaciones;

    @NotNull
    private Instant fechaCreacion;

    @NotNull
    private Boolean activo;

    @NotNull
    private LocalDate fechaAlta;

    private LocalDate fechaBaja;

    @NotNull
    private PacienteDTO paciente;

    @NotNull
    private MedicoDTO medico;

    @NotNull
    private EspecialidadDTO especialidad;

    @NotNull
    private EstadoTurnoDTO estadoTurno;

    @NotNull
    private CanalSolicitudDTO canalSolicitud;

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

    public Instant getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Instant fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public EspecialidadDTO getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(EspecialidadDTO especialidad) {
        this.especialidad = especialidad;
    }

    public EstadoTurnoDTO getEstadoTurno() {
        return estadoTurno;
    }

    public void setEstadoTurno(EstadoTurnoDTO estadoTurno) {
        this.estadoTurno = estadoTurno;
    }

    public CanalSolicitudDTO getCanalSolicitud() {
        return canalSolicitud;
    }

    public void setCanalSolicitud(CanalSolicitudDTO canalSolicitud) {
        this.canalSolicitud = canalSolicitud;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TurnoDTO)) {
            return false;
        }

        TurnoDTO turnoDTO = (TurnoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, turnoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TurnoDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", fechaHora='" + getFechaHora() + "'" +
            ", duracionMinutos=" + getDuracionMinutos() +
            ", motivoConsulta='" + getMotivoConsulta() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", activo='" + getActivo() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            ", paciente=" + getPaciente() +
            ", medico=" + getMedico() +
            ", especialidad=" + getEspecialidad() +
            ", estadoTurno=" + getEstadoTurno() +
            ", canalSolicitud=" + getCanalSolicitud() +
            "}";
    }
}
