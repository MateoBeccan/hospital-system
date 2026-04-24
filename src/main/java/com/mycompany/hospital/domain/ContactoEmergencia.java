package com.mycompany.hospital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A ContactoEmergencia.
 */
@Entity
@Table(name = "contacto_emergencia")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactoEmergencia implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 150)
    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 30)
    @Column(name = "telefono", length = 30, nullable = false)
    private String telefono;

    @Size(max = 80)
    @Column(name = "parentesco", length = 80)
    private String parentesco;

    @Size(max = 255)
    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "prioridad", nullable = false)
    private Integer prioridad;

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
    @JsonIgnoreProperties(value = { "tipoDocumento", "sexo", "ciudad", "paciente", "empleado" }, allowSetters = true)
    private Persona persona;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContactoEmergencia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public ContactoEmergencia nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public ContactoEmergencia telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getParentesco() {
        return this.parentesco;
    }

    public ContactoEmergencia parentesco(String parentesco) {
        this.setParentesco(parentesco);
        return this;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public ContactoEmergencia observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getPrioridad() {
        return this.prioridad;
    }

    public ContactoEmergencia prioridad(Integer prioridad) {
        this.setPrioridad(prioridad);
        return this;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public ContactoEmergencia activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaAlta() {
        return this.fechaAlta;
    }

    public ContactoEmergencia fechaAlta(LocalDate fechaAlta) {
        this.setFechaAlta(fechaAlta);
        return this;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return this.fechaBaja;
    }

    public ContactoEmergencia fechaBaja(LocalDate fechaBaja) {
        this.setFechaBaja(fechaBaja);
        return this;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Persona getPersona() {
        return this.persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public ContactoEmergencia persona(Persona persona) {
        this.setPersona(persona);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactoEmergencia)) {
            return false;
        }
        return getId() != null && getId().equals(((ContactoEmergencia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactoEmergencia{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", parentesco='" + getParentesco() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", prioridad=" + getPrioridad() +
            ", activo='" + getActivo() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            "}";
    }
}
