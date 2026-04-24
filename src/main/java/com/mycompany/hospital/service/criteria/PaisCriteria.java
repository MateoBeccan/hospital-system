package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.Pais} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.PaisResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pais?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaisCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter codigoIso;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private BooleanFilter activo;

    private Boolean distinct;

    public PaisCriteria() {}

    public PaisCriteria(PaisCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.codigoIso = other.optionalCodigoIso().map(StringFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaisCriteria copy() {
        return new PaisCriteria(this);
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

    public StringFilter getCodigoIso() {
        return codigoIso;
    }

    public Optional<StringFilter> optionalCodigoIso() {
        return Optional.ofNullable(codigoIso);
    }

    public StringFilter codigoIso() {
        if (codigoIso == null) {
            setCodigoIso(new StringFilter());
        }
        return codigoIso;
    }

    public void setCodigoIso(StringFilter codigoIso) {
        this.codigoIso = codigoIso;
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
        final PaisCriteria that = (PaisCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(codigoIso, that.codigoIso) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, codigoIso, fechaAlta, fechaBaja, activo, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaisCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalCodigoIso().map(f -> "codigoIso=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
