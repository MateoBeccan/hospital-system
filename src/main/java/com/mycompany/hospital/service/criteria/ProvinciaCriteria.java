package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.Provincia} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.ProvinciaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /provincias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProvinciaCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter codigo;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private BooleanFilter activo;

    private LongFilter paisId;

    private Boolean distinct;

    public ProvinciaCriteria() {}

    public ProvinciaCriteria(ProvinciaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.codigo = other.optionalCodigo().map(StringFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.paisId = other.optionalPaisId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProvinciaCriteria copy() {
        return new ProvinciaCriteria(this);
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

    public LongFilter getPaisId() {
        return paisId;
    }

    public Optional<LongFilter> optionalPaisId() {
        return Optional.ofNullable(paisId);
    }

    public LongFilter paisId() {
        if (paisId == null) {
            setPaisId(new LongFilter());
        }
        return paisId;
    }

    public void setPaisId(LongFilter paisId) {
        this.paisId = paisId;
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
        final ProvinciaCriteria that = (ProvinciaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(paisId, that.paisId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, codigo, fechaAlta, fechaBaja, activo, paisId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProvinciaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalCodigo().map(f -> "codigo=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalPaisId().map(f -> "paisId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
