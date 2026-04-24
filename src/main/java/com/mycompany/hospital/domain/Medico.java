package com.mycompany.hospital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Medico.
 */
@Entity
@Table(name = "medico")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medico implements Serializable {

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

    @Size(max = 255)
    @Column(name = "firma_digital", length = 255)
    private String firmaDigital;

    @NotNull
    @Column(name = "atiende_consultorio", nullable = false)
    private Boolean atiendeConsultorio;

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

    @ManyToOne(optional = false)
    @NotNull
    private Especialidad especialidad;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medico id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public Medico matricula(String matricula) {
        this.setMatricula(matricula);
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public LocalDate getFechaMatriculacion() {
        return this.fechaMatriculacion;
    }

    public Medico fechaMatriculacion(LocalDate fechaMatriculacion) {
        this.setFechaMatriculacion(fechaMatriculacion);
        return this;
    }

    public void setFechaMatriculacion(LocalDate fechaMatriculacion) {
        this.fechaMatriculacion = fechaMatriculacion;
    }

    public String getFirmaDigital() {
        return this.firmaDigital;
    }

    public Medico firmaDigital(String firmaDigital) {
        this.setFirmaDigital(firmaDigital);
        return this;
    }

    public void setFirmaDigital(String firmaDigital) {
        this.firmaDigital = firmaDigital;
    }

    public Boolean getAtiendeConsultorio() {
        return this.atiendeConsultorio;
    }

    public Medico atiendeConsultorio(Boolean atiendeConsultorio) {
        this.setAtiendeConsultorio(atiendeConsultorio);
        return this;
    }

    public void setAtiendeConsultorio(Boolean atiendeConsultorio) {
        this.atiendeConsultorio = atiendeConsultorio;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Medico activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaAlta() {
        return this.fechaAlta;
    }

    public Medico fechaAlta(LocalDate fechaAlta) {
        this.setFechaAlta(fechaAlta);
        return this;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return this.fechaBaja;
    }

    public Medico fechaBaja(LocalDate fechaBaja) {
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

    public Medico empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    public Especialidad getEspecialidad() {
        return this.especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Medico especialidad(Especialidad especialidad) {
        this.setEspecialidad(especialidad);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medico)) {
            return false;
        }
        return getId() != null && getId().equals(((Medico) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medico{" +
            "id=" + getId() +
            ", matricula='" + getMatricula() + "'" +
            ", fechaMatriculacion='" + getFechaMatriculacion() + "'" +
            ", firmaDigital='" + getFirmaDigital() + "'" +
            ", atiendeConsultorio='" + getAtiendeConsultorio() + "'" +
            ", activo='" + getActivo() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            "}";
    }
}
