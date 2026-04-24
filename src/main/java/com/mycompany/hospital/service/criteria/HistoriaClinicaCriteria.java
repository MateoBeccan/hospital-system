package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.HistoriaClinica} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.HistoriaClinicaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /historia-clinicas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriaClinicaCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numero;

    private LocalDateFilter fechaApertura;

    private LocalDateFilter fechaUltimaActualizacion;

    private BooleanFilter activa;

    private LocalDateFilter fechaCierre;

    private StringFilter motivoCierre;

    private LongFilter pacienteId;

    private Boolean distinct;

    public HistoriaClinicaCriteria() {}

    public HistoriaClinicaCriteria(HistoriaClinicaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numero = other.optionalNumero().map(StringFilter::copy).orElse(null);
        this.fechaApertura = other.optionalFechaApertura().map(LocalDateFilter::copy).orElse(null);
        this.fechaUltimaActualizacion = other.optionalFechaUltimaActualizacion().map(LocalDateFilter::copy).orElse(null);
        this.activa = other.optionalActiva().map(BooleanFilter::copy).orElse(null);
        this.fechaCierre = other.optionalFechaCierre().map(LocalDateFilter::copy).orElse(null);
        this.motivoCierre = other.optionalMotivoCierre().map(StringFilter::copy).orElse(null);
        this.pacienteId = other.optionalPacienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HistoriaClinicaCriteria copy() {
        return new HistoriaClinicaCriteria(this);
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

    public StringFilter getNumero() {
        return numero;
    }

    public Optional<StringFilter> optionalNumero() {
        return Optional.ofNullable(numero);
    }

    public StringFilter numero() {
        if (numero == null) {
            setNumero(new StringFilter());
        }
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
    }

    public LocalDateFilter getFechaApertura() {
        return fechaApertura;
    }

    public Optional<LocalDateFilter> optionalFechaApertura() {
        return Optional.ofNullable(fechaApertura);
    }

    public LocalDateFilter fechaApertura() {
        if (fechaApertura == null) {
            setFechaApertura(new LocalDateFilter());
        }
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateFilter fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateFilter getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public Optional<LocalDateFilter> optionalFechaUltimaActualizacion() {
        return Optional.ofNullable(fechaUltimaActualizacion);
    }

    public LocalDateFilter fechaUltimaActualizacion() {
        if (fechaUltimaActualizacion == null) {
            setFechaUltimaActualizacion(new LocalDateFilter());
        }
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(LocalDateFilter fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    public BooleanFilter getActiva() {
        return activa;
    }

    public Optional<BooleanFilter> optionalActiva() {
        return Optional.ofNullable(activa);
    }

    public BooleanFilter activa() {
        if (activa == null) {
            setActiva(new BooleanFilter());
        }
        return activa;
    }

    public void setActiva(BooleanFilter activa) {
        this.activa = activa;
    }

    public LocalDateFilter getFechaCierre() {
        return fechaCierre;
    }

    public Optional<LocalDateFilter> optionalFechaCierre() {
        return Optional.ofNullable(fechaCierre);
    }

    public LocalDateFilter fechaCierre() {
        if (fechaCierre == null) {
            setFechaCierre(new LocalDateFilter());
        }
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateFilter fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public StringFilter getMotivoCierre() {
        return motivoCierre;
    }

    public Optional<StringFilter> optionalMotivoCierre() {
        return Optional.ofNullable(motivoCierre);
    }

    public StringFilter motivoCierre() {
        if (motivoCierre == null) {
            setMotivoCierre(new StringFilter());
        }
        return motivoCierre;
    }

    public void setMotivoCierre(StringFilter motivoCierre) {
        this.motivoCierre = motivoCierre;
    }

    public LongFilter getPacienteId() {
        return pacienteId;
    }

    public Optional<LongFilter> optionalPacienteId() {
        return Optional.ofNullable(pacienteId);
    }

    public LongFilter pacienteId() {
        if (pacienteId == null) {
            setPacienteId(new LongFilter());
        }
        return pacienteId;
    }

    public void setPacienteId(LongFilter pacienteId) {
        this.pacienteId = pacienteId;
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
        final HistoriaClinicaCriteria that = (HistoriaClinicaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(fechaApertura, that.fechaApertura) &&
            Objects.equals(fechaUltimaActualizacion, that.fechaUltimaActualizacion) &&
            Objects.equals(activa, that.activa) &&
            Objects.equals(fechaCierre, that.fechaCierre) &&
            Objects.equals(motivoCierre, that.motivoCierre) &&
            Objects.equals(pacienteId, that.pacienteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numero, fechaApertura, fechaUltimaActualizacion, activa, fechaCierre, motivoCierre, pacienteId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriaClinicaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumero().map(f -> "numero=" + f + ", ").orElse("") +
            optionalFechaApertura().map(f -> "fechaApertura=" + f + ", ").orElse("") +
            optionalFechaUltimaActualizacion().map(f -> "fechaUltimaActualizacion=" + f + ", ").orElse("") +
            optionalActiva().map(f -> "activa=" + f + ", ").orElse("") +
            optionalFechaCierre().map(f -> "fechaCierre=" + f + ", ").orElse("") +
            optionalMotivoCierre().map(f -> "motivoCierre=" + f + ", ").orElse("") +
            optionalPacienteId().map(f -> "pacienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
