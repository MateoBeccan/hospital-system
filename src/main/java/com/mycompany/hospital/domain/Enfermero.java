package com.mycompany.hospital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Enfermero.
 */
@Entity
@Table(name = "enfermero")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enfermero implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 4, max = 40)
    @Column(name = "matricula", length = 40, nullable = false, unique = true)
    private String matricula;

    @Column(name = "fecha_matriculacion")
    private LocalDate fechaMatriculacion;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @NotNull
    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @JsonIgnoreProperties(value = { "persona", "tipoEmpleado", "estadoLaboral", "cargo", "medico", "enfermero" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    private TurnoLaboral turnoLaboral;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enfermero id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public Enfermero matricula(String matricula) {
        this.setMatricula(matricula);
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDate getFechaMatriculacion() {
        return this.fechaMatriculacion;
    }

    public Enfermero fechaMatriculacion(LocalDate fechaMatriculacion) {
        this.setFechaMatriculacion(fechaMatriculacion);
        return this;
    }

    public void setFechaMatriculacion(LocalDate fechaMatriculacion) {
        this.fechaMatriculacion = fechaMatriculacion;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Enfermero activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaAlta() {
        return this.fechaAlta;
    }

    public Enfermero fechaAlta(LocalDate fechaAlta) {
        this.setFechaAlta(fechaAlta);
        return this;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return this.fechaBaja;
    }

    public Enfermero fechaBaja(LocalDate fechaBaja) {
        this.setFechaBaja(fechaBaja);
        return this;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Enfermero empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    public TurnoLaboral getTurnoLaboral() {
        return this.turnoLaboral;
    }

    public void setTurnoLaboral(TurnoLaboral turnoLaboral) {
        this.turnoLaboral = turnoLaboral;
    }

    public Enfermero turnoLaboral(TurnoLaboral turnoLaboral) {
        this.setTurnoLaboral(turnoLaboral);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enfermero)) {
            return false;
        }
        return getId() != null && getId().equals(((Enfermero) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enfermero{" +
            "id=" + getId() +
            ", matricula='" + getMatricula() + "'" +
            ", fechaMatriculacion='" + getFechaMatriculacion() + "'" +
            ", activo='" + getActivo() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            "}";
    }
}
