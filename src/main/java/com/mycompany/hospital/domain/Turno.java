package com.mycompany.hospital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A Turno.
 */
@Entity
@Table(name = "turno")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Turno implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 40)
    @Column(name = "codigo", length = 40, nullable = false, unique = true)
    private String codigo;

    @NotNull
    @Column(name = "fecha_hora", nullable = false)
    private Instant fechaHora;

    @NotNull
    @Min(value = 5)
    @Max(value = 240)
    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "motivo_consulta", length = 255, nullable = false)
    private String motivoConsulta;

    @Lob
    @Column(name = "observaciones")
    private String observaciones;

    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

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
    @JsonIgnoreProperties(value = { "persona", "obraSocial", "grupoSanguineo", "factorRh", "historiaClinica" }, allowSetters = true)
    private Paciente paciente;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "empleado", "especialidad" }, allowSetters = true)
    private Medico medico;

    @ManyToOne(optional = false)
    @NotNull
    private Especialidad especialidad;

    @ManyToOne(optional = false)
    @NotNull
    private EstadoTurno estadoTurno;

    @ManyToOne(optional = false)
    @NotNull
    private CanalSolicitud canalSolicitud;

    @JsonIgnoreProperties(value = { "turno", "paciente", "medico", "historiaClinica" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "turno")
    private Consulta consulta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Turno id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Turno codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Instant getFechaHora() {
        return this.fechaHora;
    }

    public Turno fechaHora(Instant fechaHora) {
        this.setFechaHora(fechaHora);
        return this;
    }

    public void setFechaHora(Instant fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getDuracionMinutos() {
        return this.duracionMinutos;
    }

    public Turno duracionMinutos(Integer duracionMinutos) {
        this.setDuracionMinutos(duracionMinutos);
        return this;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getMotivoConsulta() {
        return this.motivoConsulta;
    }

    public Turno motivoConsulta(String motivoConsulta) {
        this.setMotivoConsulta(motivoConsulta);
        return this;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Turno observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Instant getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Turno fechaCreacion(Instant fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Turno activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaAlta() {
        return this.fechaAlta;
    }

    public Turno fechaAlta(LocalDate fechaAlta) {
        this.setFechaAlta(fechaAlta);
        return this;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return this.fechaBaja;
    }

    public Turno fechaBaja(LocalDate fechaBaja) {
        this.setFechaBaja(fechaBaja);
        return this;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Turno paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Turno medico(Medico medico) {
        this.setMedico(medico);
        return this;
    }

    public Especialidad getEspecialidad() {
        return this.especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Turno especialidad(Especialidad especialidad) {
        this.setEspecialidad(especialidad);
        return this;
    }

    public EstadoTurno getEstadoTurno() {
        return this.estadoTurno;
    }

    public void setEstadoTurno(EstadoTurno estadoTurno) {
        this.estadoTurno = estadoTurno;
    }

    public Turno estadoTurno(EstadoTurno estadoTurno) {
        this.setEstadoTurno(estadoTurno);
        return this;
    }

    public CanalSolicitud getCanalSolicitud() {
        return this.canalSolicitud;
    }

    public void setCanalSolicitud(CanalSolicitud canalSolicitud) {
        this.canalSolicitud = canalSolicitud;
    }

    public Turno canalSolicitud(CanalSolicitud canalSolicitud) {
        this.setCanalSolicitud(canalSolicitud);
        return this;
    }

    public Consulta getConsulta() {
        return this.consulta;
    }

    public void setConsulta(Consulta consulta) {
        if (this.consulta != null) {
            this.consulta.setTurno(null);
        }
        if (consulta != null) {
            consulta.setTurno(this);
        }
        this.consulta = consulta;
    }

    public Turno consulta(Consulta consulta) {
        this.setConsulta(consulta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Turno)) {
            return false;
        }
        return getId() != null && getId().equals(((Turno) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Turno{" +
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
            "}";
    }
}
