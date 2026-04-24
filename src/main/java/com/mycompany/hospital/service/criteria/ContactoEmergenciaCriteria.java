package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.ContactoEmergencia} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.ContactoEmergenciaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contacto-emergencias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactoEmergenciaCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter telefono;

    private StringFilter parentesco;

    private StringFilter observaciones;

    private IntegerFilter prioridad;

    private BooleanFilter activo;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private LongFilter personaId;

    private Boolean distinct;

    public ContactoEmergenciaCriteria() {}

    public ContactoEmergenciaCriteria(ContactoEmergenciaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.parentesco = other.optionalParentesco().map(StringFilter::copy).orElse(null);
        this.observaciones = other.optionalObservaciones().map(StringFilter::copy).orElse(null);
        this.prioridad = other.optionalPrioridad().map(IntegerFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.personaId = other.optionalPersonaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ContactoEmergenciaCriteria copy() {
        return new ContactoEmergenciaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public Optional<StringFilter> optionalTelefono() {
        return Optional.ofNullable(telefono);
    }

    public StringFilter telefono() {
        if (telefono == null) {
            setTelefono(new StringFilter());
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public StringFilter getParentesco() {
        return parentesco;
    }

    public Optional<StringFilter> optionalParentesco() {
        return Optional.ofNullable(parentesco);
    }

    public StringFilter parentesco() {
        if (parentesco == null) {
            setParentesco(new StringFilter());
        }
        return parentesco;
    }

    public void setParentesco(StringFilter parentesco) {
        this.parentesco = parentesco;
    }

    public StringFilter getObservaciones() {
        return observaciones;
    }

    public Optional<StringFilter> optionalObservaciones() {
        return Optional.ofNullable(observaciones);
    }

    public StringFilter observaciones() {
        if (observaciones == null) {
            setObservaciones(new StringFilter());
        }
        return observaciones;
    }

    public void setObservaciones(StringFilter observaciones) {
        this.observaciones = observaciones;
    }

    public IntegerFilter getPrioridad() {
        return prioridad;
    }

    public Optional<IntegerFilter> optionalPrioridad() {
        return Optional.ofNullable(prioridad);
    }

    public IntegerFilter prioridad() {
        if (prioridad == null) {
            setPrioridad(new IntegerFilter());
        }
        return prioridad;
    }

    public void setPrioridad(IntegerFilter prioridad) {
        this.prioridad = prioridad;
    }

    public BooleanFilter getActivo() {
        return activo;
    }

    public Optional<BooleanFilter> optionalActivo() {
        return Optional.ofNullable(activo);
    }

    public BooleanFilter activo() {
        if (activo == null) {
            setActivo(new BooleanFilter());
        }
        return activo;
    }

    public void setActivo(BooleanFilter activo) {
        this.activo = activo;
    }

    public LocalDateFilter getFechaAlta() {
        return fechaAlta;
    }

    public Optional<LocalDateFilter> optionalFechaAlta() {
        return Optional.ofNullable(fechaAlta);
    }

    public LocalDateFilter fechaAlta() {
        if (fechaAlta == null) {
            setFechaAlta(new LocalDateFilter());
        }
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateFilter fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDateFilter getFechaBaja() {
        return fechaBaja;
    }

    public Optional<LocalDateFilter> optionalFechaBaja() {
        return Optional.ofNullable(fechaBaja);
    }

    public LocalDateFilter fechaBaja() {
        if (fechaBaja == null) {
            setFechaBaja(new LocalDateFilter());
        }
        return fechaBaja;
    }

    public void setFechaBaja(LocalDateFilter fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public LongFilter getPersonaId() {
        return personaId;
    }

    public Optional<LongFilter> optionalPersonaId() {
        return Optional.ofNullable(personaId);
    }

    public LongFilter personaId() {
        if (personaId == null) {
            setPersonaId(new LongFilter());
        }
        return personaId;
    }

    public void setPersonaId(LongFilter personaId) {
        this.personaId = personaId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ContactoEmergenciaCriteria that = (ContactoEmergenciaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(parentesco, that.parentesco) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(prioridad, that.prioridad) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(personaId, that.personaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, telefono, parentesco, observaciones, prioridad, activo, fechaAlta, fechaBaja, personaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactoEmergenciaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalParentesco().map(f -> "parentesco=" + f + ", ").orElse("") +
            optionalObservaciones().map(f -> "observaciones=" + f + ", ").orElse("") +
            optionalPrioridad().map(f -> "prioridad=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalPersonaId().map(f -> "personaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
