package com.mycompany.hospital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A Consulta.
 */
@Entity
@Table(name = "consulta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Consulta implements Serializable {

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
    @Column(name = "fecha_hora_inicio", nullable = false)
    private Instant fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private Instant fechaHoraFin;

    @Lob
    @Column(name = "sintomas")
    private String sintomas;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "motivo_consulta", length = 255, nullable = false)
    private String motivoConsulta;

    @Lob
    @Column(name = "examen_fisico")
    private String examenFisico;

    @Lob
    @Column(name = "observaciones")
    private String observaciones;

    @Lob
    @Column(name = "indicaciones")
    private String indicaciones;

    @NotNull
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @NotNull
    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @JsonIgnoreProperties(
        value = { "paciente", "medico", "especialidad", "estadoTurno", "canalSolicitud", "consulta" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Turno turno;

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
    @JsonIgnoreProperties(value = { "paciente" }, allowSetters = true)
    private HistoriaClinica historiaClinica;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Consulta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Consulta codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Instant getFechaHoraInicio() {
        return this.fechaHoraInicio;
    }

    public Consulta fechaHoraInicio(Instant fechaHoraInicio) {
        this.setFechaHoraInicio(fechaHoraInicio);
        return this;
    }

    public void setFechaHoraInicio(Instant fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Instant getFechaHoraFin() {
        return this.fechaHoraFin;
    }

    public Consulta fechaHoraFin(Instant fechaHoraFin) {
        this.setFechaHoraFin(fechaHoraFin);
        return this;
    }

    public void setFechaHoraFin(Instant fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public String getSintomas() {
        return this.sintomas;
    }

    public Consulta sintomas(String sintomas) {
        this.setSintomas(sintomas);
        return this;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getMotivoConsulta() {
        return this.motivoConsulta;
    }

    public Consulta motivoConsulta(String motivoConsulta) {
        this.setMotivoConsulta(motivoConsulta);
        return this;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getExamenFisico() {
        return this.examenFisico;
    }

    public Consulta examenFisico(String examenFisico) {
        this.setExamenFisico(examenFisico);
        return this;
    }

    public void setExamenFisico(String examenFisico) {
        this.examenFisico = examenFisico;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Consulta observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getIndicaciones() {
        return this.indicaciones;
    }

    public Consulta indicaciones(String indicaciones) {
        this.setIndicaciones(indicaciones);
        return this;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public Boolean getActiva() {
        return this.activa;
    }

    public Consulta activa(Boolean activa) {
        this.setActiva(activa);
        return this;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public LocalDate getFechaAlta() {
        return this.fechaAlta;
    }

    public Consulta fechaAlta(LocalDate fechaAlta) {
        this.setFechaAlta(fechaAlta);
        return this;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return this.fechaBaja;
    }

    public Consulta fechaBaja(LocalDate fechaBaja) {
        this.setFechaBaja(fechaBaja);
        return this;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Turno getTurno() {
        return this.turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Consulta turno(Turno turno) {
        this.setTurno(turno);
        return this;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Consulta paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Consulta medico(Medico medico) {
        this.setMedico(medico);
        return this;
    }

    public HistoriaClinica getHistoriaClinica() {
        return this.historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    public Consulta historiaClinica(HistoriaClinica historiaClinica) {
        this.setHistoriaClinica(historiaClinica);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consulta)) {
            return false;
        }
        return getId() != null && getId().equals(((Consulta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consulta{" +
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
            "}";
    }
}
