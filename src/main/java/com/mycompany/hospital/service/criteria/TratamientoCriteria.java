package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.Tratamiento} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.TratamientoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tratamientos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TratamientoCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codigo;

    private StringFilter descripcion;

    private LocalDateFilter fechaInicio;

    private LocalDateFilter fechaFin;

    private LocalDateFilter fechaProximaRevision;

    private BooleanFilter activo;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private LongFilter diagnosticoId;

    private LongFilter estadoTratamientoId;

    private Boolean distinct;

    public TratamientoCriteria() {}

    public TratamientoCriteria(TratamientoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.codigo = other.optionalCodigo().map(StringFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.fechaInicio = other.optionalFechaInicio().map(LocalDateFilter::copy).orElse(null);
        this.fechaFin = other.optionalFechaFin().map(LocalDateFilter::copy).orElse(null);
        this.fechaProximaRevision = other.optionalFechaProximaRevision().map(LocalDateFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.diagnosticoId = other.optionalDiagnosticoId().map(LongFilter::copy).orElse(null);
        this.estadoTratamientoId = other.optionalEstadoTratamientoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TratamientoCriteria copy() {
        return new TratamientoCriteria(this);
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

    public StringFilter getCodigo() {
        return codigo;
    }

    public Optional<StringFilter> optionalCodigo() {
        return Optional.ofNullable(codigo);
    }

    public StringFilter codigo() {
        if (codigo == null) {
            setCodigo(new StringFilter());
        }
        return codigo;
    }

    public void setCodigo(StringFilter codigo) {
        this.codigo = codigo;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateFilter getFechaInicio() {
        return fechaInicio;
    }

    public Optional<LocalDateFilter> optionalFechaInicio() {
        return Optional.ofNullable(fechaInicio);
    }

    public LocalDateFilter fechaInicio() {
        if (fechaInicio == null) {
            setFechaInicio(new LocalDateFilter());
        }
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateFilter fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateFilter getFechaFin() {
        return fechaFin;
    }

    public Optional<LocalDateFilter> optionalFechaFin() {
        return Optional.ofNullable(fechaFin);
    }

    public LocalDateFilter fechaFin() {
        if (fechaFin == null) {
            setFechaFin(new LocalDateFilter());
        }
        return fechaFin;
    }

    public void setFechaFin(LocalDateFilter fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDateFilter getFechaProximaRevision() {
        return fechaProximaRevision;
    }

    public Optional<LocalDateFilter> optionalFechaProximaRevision() {
        return Optional.ofNullable(fechaProximaRevision);
    }

    public LocalDateFilter fechaProximaRevision() {
        if (fechaProximaRevision == null) {
            setFechaProximaRevision(new LocalDateFilter());
        }
        return fechaProximaRevision;
    }

    public void setFechaProximaRevision(LocalDateFilter fechaProximaRevision) {
        this.fechaProximaRevision = fechaProximaRevision;
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

    public LongFilter getDiagnosticoId() {
        return diagnosticoId;
    }

    public Optional<LongFilter> optionalDiagnosticoId() {
        return Optional.ofNullable(diagnosticoId);
    }

    public LongFilter diagnosticoId() {
        if (diagnosticoId == null) {
            setDiagnosticoId(new LongFilter());
        }
        return diagnosticoId;
    }

    public void setDiagnosticoId(LongFilter diagnosticoId) {
        this.diagnosticoId = diagnosticoId;
    }

    public LongFilter getEstadoTratamientoId() {
        return estadoTratamientoId;
    }

    public Optional<LongFilter> optionalEstadoTratamientoId() {
        return Optional.ofNullable(estadoTratamientoId);
    }

    public LongFilter estadoTratamientoId() {
        if (estadoTratamientoId == null) {
            setEstadoTratamientoId(new LongFilter());
        }
        return estadoTratamientoId;
    }

    public void setEstadoTratamientoId(LongFilter estadoTratamientoId) {
        this.estadoTratamientoId = estadoTratamientoId;
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
        final TratamientoCriteria that = (TratamientoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(fechaInicio, that.fechaInicio) &&
            Objects.equals(fechaFin, that.fechaFin) &&
            Objects.equals(fechaProximaRevision, that.fechaProximaRevision) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(diagnosticoId, that.diagnosticoId) &&
            Objects.equals(estadoTratamientoId, that.estadoTratamientoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codigo,
            descripcion,
            fechaInicio,
            fechaFin,
            fechaProximaRevision,
            activo,
            fechaAlta,
            fechaBaja,
            diagnosticoId,
            estadoTratamientoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TratamientoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCodigo().map(f -> "codigo=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalFechaInicio().map(f -> "fechaInicio=" + f + ", ").orElse("") +
            optionalFechaFin().map(f -> "fechaFin=" + f + ", ").orElse("") +
            optionalFechaProximaRevision().map(f -> "fechaProximaRevision=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalDiagnosticoId().map(f -> "diagnosticoId=" + f + ", ").orElse("") +
            optionalEstadoTratamientoId().map(f -> "estadoTratamientoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
