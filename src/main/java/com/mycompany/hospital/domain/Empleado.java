package com.mycompany.hospital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Empleado.
 */
@Entity
@Table(name = "empleado")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Empleado implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "legajo", length = 30, nullable = false, unique = true)
    private String legajo;

    @NotNull
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @JsonIgnoreProperties(value = { "tipoDocumento", "sexo", "ciudad", "paciente", "empleado" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Persona persona;

    @ManyToOne(optional = false)
    @NotNull
    private TipoEmpleado tipoEmpleado;

    @ManyToOne(optional = false)
    @NotNull
    private EstadoLaboral estadoLaboral;

    @ManyToOne(optional = false)
    @NotNull
    private Cargo cargo;

    @JsonIgnoreProperties(value = { "empleado", "especialidad" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "empleado")
    private Medico medico;

    @JsonIgnoreProperties(value = { "empleado", "turnoLaboral" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "empleado")
    private Enfermero enfermero;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empleado id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLegajo() {
        return this.legajo;
    }

    public Empleado legajo(String legajo) {
        this.setLegajo(legajo);
        return this;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public LocalDate getFechaIngreso() {
        return this.fechaIngreso;
    }

    public Empleado fechaIngreso(LocalDate fechaIngreso) {
        this.setFechaIngreso(fechaIngreso);
        return this;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaBaja() {
        return this.fechaBaja;
    }

    public Empleado fechaBaja(LocalDate fechaBaja) {
        this.setFechaBaja(fechaBaja);
        return this;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Empleado activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Persona getPersona() {
        return this.persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Empleado persona(Persona persona) {
        this.setPersona(persona);
        return this;
    }

    public TipoEmpleado getTipoEmpleado() {
        return this.tipoEmpleado;
    }

    public void setTipoEmpleado(TipoEmpleado tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    public Empleado tipoEmpleado(TipoEmpleado tipoEmpleado) {
        this.setTipoEmpleado(tipoEmpleado);
        return this;
    }

    public EstadoLaboral getEstadoLaboral() {
        return this.estadoLaboral;
    }

    public void setEstadoLaboral(EstadoLaboral estadoLaboral) {
        this.estadoLaboral = estadoLaboral;
    }

    public Empleado estadoLaboral(EstadoLaboral estadoLaboral) {
        this.setEstadoLaboral(estadoLaboral);
        return this;
    }

    public Cargo getCargo() {
        return this.cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Empleado cargo(Cargo cargo) {
        this.setCargo(cargo);
        return this;
    }

    public Medico getMedico() {
        return this.medico;
    }

    public void setMedico(Medico medico) {
        if (this.medico != null) {
            this.medico.setEmpleado(null);
        }
        if (medico != null) {
            medico.setEmpleado(this);
        }
        this.medico = medico;
    }

    public Empleado medico(Medico medico) {
        this.setMedico(medico);
        return this;
    }

    public Enfermero getEnfermero() {
        return this.enfermero;
    }

    public void setEnfermero(Enfermero enfermero) {
        if (this.enfermero != null) {
            this.enfermero.setEmpleado(null);
        }
        if (enfermero != null) {
            enfermero.setEmpleado(this);
        }
        this.enfermero = enfermero;
    }

    public Empleado enfermero(Enfermero enfermero) {
        this.setEnfermero(enfermero);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empleado)) {
            return false;
        }
        return getId() != null && getId().equals(((Empleado) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empleado{" +
            "id=" + getId() +
            ", legajo='" + getLegajo() + "'" +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
