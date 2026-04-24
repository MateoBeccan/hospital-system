package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.Enfermero} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.EnfermeroResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /enfermeros?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnfermeroCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matricula;

    private LocalDateFilter fechaMatriculacion;

    private BooleanFilter activo;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private LongFilter empleadoId;

    private LongFilter turnoLaboralId;

    private Boolean distinct;

    public EnfermeroCriteria() {}

    public EnfermeroCriteria(EnfermeroCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.matricula = other.optionalMatricula().map(StringFilter::copy).orElse(null);
        this.fechaMatriculacion = other.optionalFechaMatriculacion().map(LocalDateFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.empleadoId = other.optionalEmpleadoId().map(LongFilter::copy).orElse(null);
        this.turnoLaboralId = other.optionalTurnoLaboralId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EnfermeroCriteria copy() {
        return new EnfermeroCriteria(this);
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

    public StringFilter getMatricula() {
        return matricula;
    }

    public Optional<StringFilter> optionalMatricula() {
        return Optional.ofNullable(matricula);
    }

    public StringFilter matricula() {
        if (matricula == null) {
            setMatricula(new StringFilter());
        }
        return matricula;
    }

    public void setMatricula(StringFilter matricula) {
        this.matricula = matricula;
    }

    public LocalDateFilter getFechaMatriculacion() {
        return fechaMatriculacion;
    }

    public Optional<LocalDateFilter> optionalFechaMatriculacion() {
        return Optional.ofNullable(fechaMatriculacion);
    }

    public LocalDateFilter fechaMatriculacion() {
        if (fechaMatriculacion == null) {
            setFechaMatriculacion(new LocalDateFilter());
        }
        return fechaMatriculacion;
    }

    public void setFechaMatriculacion(LocalDateFilter fechaMatriculacion) {
        this.fechaMatriculacion = fechaMatriculacion;
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

    public LongFilter getEmpleadoId() {
        return empleadoId;
    }

    public Optional<LongFilter> optionalEmpleadoId() {
        return Optional.ofNullable(empleadoId);
    }

    public LongFilter empleadoId() {
        if (empleadoId == null) {
            setEmpleadoId(new LongFilter());
        }
        return empleadoId;
    }

    public void setEmpleadoId(LongFilter empleadoId) {
        this.empleadoId = empleadoId;
    }

    public LongFilter getTurnoLaboralId() {
        return turnoLaboralId;
    }

    public Optional<LongFilter> optionalTurnoLaboralId() {
        return Optional.ofNullable(turnoLaboralId);
    }

    public LongFilter turnoLaboralId() {
        if (turnoLaboralId == null) {
            setTurnoLaboralId(new LongFilter());
        }
        return turnoLaboralId;
    }

    public void setTurnoLaboralId(LongFilter turnoLaboralId) {
        this.turnoLaboralId = turnoLaboralId;
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
        final EnfermeroCriteria that = (EnfermeroCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(matricula, that.matricula) &&
            Objects.equals(fechaMatriculacion, that.fechaMatriculacion) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(empleadoId, that.empleadoId) &&
            Objects.equals(turnoLaboralId, that.turnoLaboralId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matricula, fechaMatriculacion, activo, fechaAlta, fechaBaja, empleadoId, turnoLaboralId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnfermeroCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMatricula().map(f -> "matricula=" + f + ", ").orElse("") +
            optionalFechaMatriculacion().map(f -> "fechaMatriculacion=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalEmpleadoId().map(f -> "empleadoId=" + f + ", ").orElse("") +
            optionalTurnoLaboralId().map(f -> "turnoLaboralId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
