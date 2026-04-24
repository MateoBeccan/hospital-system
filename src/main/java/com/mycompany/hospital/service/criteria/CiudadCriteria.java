package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.Ciudad} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.CiudadResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ciudads?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CiudadCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter codigo;

    private StringFilter codigoPostal;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private BooleanFilter activo;

    private LongFilter provinciaId;

    private Boolean distinct;

    public CiudadCriteria() {}

    public CiudadCriteria(CiudadCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.codigo = other.optionalCodigo().map(StringFilter::copy).orElse(null);
        this.codigoPostal = other.optionalCodigoPostal().map(StringFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.provinciaId = other.optionalProvinciaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CiudadCriteria copy() {
        return new CiudadCriteria(this);
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

    public StringFilter getCodigoPostal() {
        return codigoPostal;
    }

    public Optional<StringFilter> optionalCodigoPostal() {
        return Optional.ofNullable(codigoPostal);
    }

    public StringFilter codigoPostal() {
        if (codigoPostal == null) {
            setCodigoPostal(new StringFilter());
        }
        return codigoPostal;
    }

    public void setCodigoPostal(StringFilter codigoPostal) {
        this.codigoPostal = codigoPostal;
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

    public LongFilter getProvinciaId() {
        return provinciaId;
    }

    public Optional<LongFilter> optionalProvinciaId() {
        return Optional.ofNullable(provinciaId);
    }

    public LongFilter provinciaId() {
        if (provinciaId == null) {
            setProvinciaId(new LongFilter());
        }
        return provinciaId;
    }

    public void setProvinciaId(LongFilter provinciaId) {
        this.provinciaId = provinciaId;
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
        final CiudadCriteria that = (CiudadCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(codigoPostal, that.codigoPostal) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(provinciaId, that.provinciaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, codigo, codigoPostal, fechaAlta, fechaBaja, activo, provinciaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CiudadCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalCodigo().map(f -> "codigo=" + f + ", ").orElse("") +
            optionalCodigoPostal().map(f -> "codigoPostal=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalProvinciaId().map(f -> "provinciaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
