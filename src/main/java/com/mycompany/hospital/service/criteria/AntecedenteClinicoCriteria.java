package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.AntecedenteClinico} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.AntecedenteClinicoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /antecedente-clinicos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AntecedenteClinicoCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titulo;

    private StringFilter descripcion;

    private LocalDateFilter fechaRegistro;

    private BooleanFilter activo;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private LongFilter historiaClinicaId;

    private Boolean distinct;

    public AntecedenteClinicoCriteria() {}

    public AntecedenteClinicoCriteria(AntecedenteClinicoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titulo = other.optionalTitulo().map(StringFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.fechaRegistro = other.optionalFechaRegistro().map(LocalDateFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.historiaClinicaId = other.optionalHistoriaClinicaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AntecedenteClinicoCriteria copy() {
        return new AntecedenteClinicoCriteria(this);
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

    public StringFilter getTitulo() {
        return titulo;
    }

    public Optional<StringFilter> optionalTitulo() {
        return Optional.ofNullable(titulo);
    }

    public StringFilter titulo() {
        if (titulo == null) {
            setTitulo(new StringFilter());
        }
        return titulo;
    }

    public void setTitulo(StringFilter titulo) {
        this.titulo = titulo;
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

    public LocalDateFilter getFechaRegistro() {
        return fechaRegistro;
    }

    public Optional<LocalDateFilter> optionalFechaRegistro() {
        return Optional.ofNullable(fechaRegistro);
    }

    public LocalDateFilter fechaRegistro() {
        if (fechaRegistro == null) {
            setFechaRegistro(new LocalDateFilter());
        }
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateFilter fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    public LongFilter getHistoriaClinicaId() {
        return historiaClinicaId;
    }

    public Optional<LongFilter> optionalHistoriaClinicaId() {
        return Optional.ofNullable(historiaClinicaId);
    }

    public LongFilter historiaClinicaId() {
        if (historiaClinicaId == null) {
            setHistoriaClinicaId(new LongFilter());
        }
        return historiaClinicaId;
    }

    public void setHistoriaClinicaId(LongFilter historiaClinicaId) {
        this.historiaClinicaId = historiaClinicaId;
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
        final AntecedenteClinicoCriteria that = (AntecedenteClinicoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titulo, that.titulo) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(historiaClinicaId, that.historiaClinicaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descripcion, fechaRegistro, activo, fechaAlta, fechaBaja, historiaClinicaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AntecedenteClinicoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitulo().map(f -> "titulo=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalFechaRegistro().map(f -> "fechaRegistro=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalHistoriaClinicaId().map(f -> "historiaClinicaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
