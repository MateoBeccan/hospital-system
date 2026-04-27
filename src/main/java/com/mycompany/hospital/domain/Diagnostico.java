package com.mycompany.hospital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Diagnostico.
 */
@Entity
@Table(name = "diagnostico")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Diagnostico implements Serializable {

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
    @Column(name = "fecha_diagnostico", nullable = false)
    private LocalDate fechaDiagnostico;

    @NotNull
    @Size(min = 3, max = 255)
    @Column(name = "descripcion", length = 255, nullable = false)
    private String descripcion;

    @Lob
    @Column(name = "observaciones")
    private String observaciones;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "fecha_resolucion")
    private LocalDate fechaResolucion;

    @NotNull
    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal;

    @NotNull
    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "turno", "paciente", "medico", "historiaClinica" }, allowSetters = true)
    private Consulta consulta;

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
    private TipoDiagnostico tipoDiagnostico;

    @ManyToOne(optional = false)
    @NotNull
    private EstadoDiagnostico estadoDiagnostico;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Diagnostico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Diagnostico codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFechaDiagnostico() {
        return this.fechaDiagnostico;
    }

    public Diagnostico fechaDiagnostico(LocalDate fechaDiagnostico) {
        this.setFechaDiagnostico(fechaDiagnostico);
        return this;
    }

    public void setFechaDiagnostico(LocalDate fechaDiagnostico) {
        this.fechaDiagnostico = fechaDiagnostico;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Diagnostico descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Diagnostico observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Diagnostico activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaResolucion() {
        return this.fechaResolucion;
    }

    public Diagnostico fechaResolucion(LocalDate fechaResolucion) {
        this.setFechaResolucion(fechaResolucion);
        return this;
    }

    public void setFechaResolucion(LocalDate fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public Boolean getEsPrincipal() {
        return this.esPrincipal;
    }

    public Diagnostico esPrincipal(Boolean esPrincipal) {
        this.setEsPrincipal(esPrincipal);
        return this;
    }

    public void setEsPrincipal(Boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }

    public LocalDate getFechaAlta() {
        return this.fechaAlta;
    }

    public Diagnostico fechaAlta(LocalDate fechaAlta) {
        this.setFechaAlta(fechaAlta);
        return this;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return this.fechaBaja;
    }

    public Diagnostico fechaBaja(LocalDate fechaBaja) {
        this.setFechaBaja(fechaBaja);
        return this;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Consulta getConsulta() {
        return this.consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public Diagnostico consulta(Consulta consulta) {
        this.setConsulta(consulta);
        return this;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Diagnostico paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Diagnostico medico(Medico medico) {
        this.setMedico(medico);
        return this;
    }

    public TipoDiagnostico getTipoDiagnostico() {
        return this.tipoDiagnostico;
    }

    public void setTipoDiagnostico(TipoDiagnostico tipoDiagnostico) {
        this.tipoDiagnostico = tipoDiagnostico;
    }

    public Diagnostico tipoDiagnostico(TipoDiagnostico tipoDiagnostico) {
        this.setTipoDiagnostico(tipoDiagnostico);
        return this;
    }

    public EstadoDiagnostico getEstadoDiagnostico() {
        return this.estadoDiagnostico;
    }

    public void setEstadoDiagnostico(EstadoDiagnostico estadoDiagnostico) {
        this.estadoDiagnostico = estadoDiagnostico;
    }

    public Diagnostico estadoDiagnostico(EstadoDiagnostico estadoDiagnostico) {
        this.setEstadoDiagnostico(estadoDiagnostico);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Diagnostico)) {
            return false;
        }
        return getId() != null && getId().equals(((Diagnostico) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Diagnostico{" +
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
            "}";
    }
}
