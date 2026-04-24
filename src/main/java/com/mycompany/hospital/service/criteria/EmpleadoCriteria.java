package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.Empleado} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.EmpleadoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /empleados?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmpleadoCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter legajo;

    private LocalDateFilter fechaIngreso;

    private LocalDateFilter fechaBaja;

    private BooleanFilter activo;

    private LongFilter personaId;

    private LongFilter tipoEmpleadoId;

    private LongFilter estadoLaboralId;

    private LongFilter cargoId;

    private LongFilter medicoId;

    private LongFilter enfermeroId;

    private Boolean distinct;

    public EmpleadoCriteria() {}

    public EmpleadoCriteria(EmpleadoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.legajo = other.optionalLegajo().map(StringFilter::copy).orElse(null);
        this.fechaIngreso = other.optionalFechaIngreso().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.personaId = other.optionalPersonaId().map(LongFilter::copy).orElse(null);
        this.tipoEmpleadoId = other.optionalTipoEmpleadoId().map(LongFilter::copy).orElse(null);
        this.estadoLaboralId = other.optionalEstadoLaboralId().map(LongFilter::copy).orElse(null);
        this.cargoId = other.optionalCargoId().map(LongFilter::copy).orElse(null);
        this.medicoId = other.optionalMedicoId().map(LongFilter::copy).orElse(null);
        this.enfermeroId = other.optionalEnfermeroId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EmpleadoCriteria copy() {
        return new EmpleadoCriteria(this);
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

    public StringFilter getLegajo() {
        return legajo;
    }

    public Optional<StringFilter> optionalLegajo() {
        return Optional.ofNullable(legajo);
    }

    public StringFilter legajo() {
        if (legajo == null) {
            setLegajo(new StringFilter());
        }
        return legajo;
    }

    public void setLegajo(StringFilter legajo) {
        this.legajo = legajo;
    }

    public LocalDateFilter getFechaIngreso() {
        return fechaIngreso;
    }

    public Optional<LocalDateFilter> optionalFechaIngreso() {
        return Optional.ofNullable(fechaIngreso);
    }

    public LocalDateFilter fechaIngreso() {
        if (fechaIngreso == null) {
            setFechaIngreso(new LocalDateFilter());
        }
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateFilter fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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

    public LongFilter getTipoEmpleadoId() {
        return tipoEmpleadoId;
    }

    public Optional<LongFilter> optionalTipoEmpleadoId() {
        return Optional.ofNullable(tipoEmpleadoId);
    }

    public LongFilter tipoEmpleadoId() {
        if (tipoEmpleadoId == null) {
            setTipoEmpleadoId(new LongFilter());
        }
        return tipoEmpleadoId;
    }

    public void setTipoEmpleadoId(LongFilter tipoEmpleadoId) {
        this.tipoEmpleadoId = tipoEmpleadoId;
    }

    public LongFilter getEstadoLaboralId() {
        return estadoLaboralId;
    }

    public Optional<LongFilter> optionalEstadoLaboralId() {
        return Optional.ofNullable(estadoLaboralId);
    }

    public LongFilter estadoLaboralId() {
        if (estadoLaboralId == null) {
            setEstadoLaboralId(new LongFilter());
        }
        return estadoLaboralId;
    }

    public void setEstadoLaboralId(LongFilter estadoLaboralId) {
        this.estadoLaboralId = estadoLaboralId;
    }

    public LongFilter getCargoId() {
        return cargoId;
    }

    public Optional<LongFilter> optionalCargoId() {
        return Optional.ofNullable(cargoId);
    }

    public LongFilter cargoId() {
        if (cargoId == null) {
            setCargoId(new LongFilter());
        }
        return cargoId;
    }

    public void setCargoId(LongFilter cargoId) {
        this.cargoId = cargoId;
    }

    public LongFilter getMedicoId() {
        return medicoId;
    }

    public Optional<LongFilter> optionalMedicoId() {
        return Optional.ofNullable(medicoId);
    }

    public LongFilter medicoId() {
        if (medicoId == null) {
            setMedicoId(new LongFilter());
        }
        return medicoId;
    }

    public void setMedicoId(LongFilter medicoId) {
        this.medicoId = medicoId;
    }

    public LongFilter getEnfermeroId() {
        return enfermeroId;
    }

    public Optional<LongFilter> optionalEnfermeroId() {
        return Optional.ofNullable(enfermeroId);
    }

    public LongFilter enfermeroId() {
        if (enfermeroId == null) {
            setEnfermeroId(new LongFilter());
        }
        return enfermeroId;
    }

    public void setEnfermeroId(LongFilter enfermeroId) {
        this.enfermeroId = enfermeroId;
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
        final EmpleadoCriteria that = (EmpleadoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(legajo, that.legajo) &&
            Objects.equals(fechaIngreso, that.fechaIngreso) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(personaId, that.personaId) &&
            Objects.equals(tipoEmpleadoId, that.tipoEmpleadoId) &&
            Objects.equals(estadoLaboralId, that.estadoLaboralId) &&
            Objects.equals(cargoId, that.cargoId) &&
            Objects.equals(medicoId, that.medicoId) &&
            Objects.equals(enfermeroId, that.enfermeroId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            legajo,
            fechaIngreso,
            fechaBaja,
            activo,
            personaId,
            tipoEmpleadoId,
            estadoLaboralId,
            cargoId,
            medicoId,
            enfermeroId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpleadoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLegajo().map(f -> "legajo=" + f + ", ").orElse("") +
            optionalFechaIngreso().map(f -> "fechaIngreso=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalPersonaId().map(f -> "personaId=" + f + ", ").orElse("") +
            optionalTipoEmpleadoId().map(f -> "tipoEmpleadoId=" + f + ", ").orElse("") +
            optionalEstadoLaboralId().map(f -> "estadoLaboralId=" + f + ", ").orElse("") +
            optionalCargoId().map(f -> "cargoId=" + f + ", ").orElse("") +
            optionalMedicoId().map(f -> "medicoId=" + f + ", ").orElse("") +
            optionalEnfermeroId().map(f -> "enfermeroId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
