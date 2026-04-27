package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.SignosVitales} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.SignosVitalesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /signos-vitales?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SignosVitalesCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter fechaHoraRegistro;

    private BigDecimalFilter peso;

    private BigDecimalFilter talla;

    private BigDecimalFilter temperatura;

    private StringFilter presionArterial;

    private IntegerFilter frecuenciaCardiaca;

    private IntegerFilter frecuenciaRespiratoria;

    private IntegerFilter saturacionOxigeno;

    private BooleanFilter activo;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private LongFilter consultaId;

    private Boolean distinct;

    public SignosVitalesCriteria() {}

    public SignosVitalesCriteria(SignosVitalesCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fechaHoraRegistro = other.optionalFechaHoraRegistro().map(InstantFilter::copy).orElse(null);
        this.peso = other.optionalPeso().map(BigDecimalFilter::copy).orElse(null);
        this.talla = other.optionalTalla().map(BigDecimalFilter::copy).orElse(null);
        this.temperatura = other.optionalTemperatura().map(BigDecimalFilter::copy).orElse(null);
        this.presionArterial = other.optionalPresionArterial().map(StringFilter::copy).orElse(null);
        this.frecuenciaCardiaca = other.optionalFrecuenciaCardiaca().map(IntegerFilter::copy).orElse(null);
        this.frecuenciaRespiratoria = other.optionalFrecuenciaRespiratoria().map(IntegerFilter::copy).orElse(null);
        this.saturacionOxigeno = other.optionalSaturacionOxigeno().map(IntegerFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.consultaId = other.optionalConsultaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SignosVitalesCriteria copy() {
        return new SignosVitalesCriteria(this);
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

    public InstantFilter getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public Optional<InstantFilter> optionalFechaHoraRegistro() {
        return Optional.ofNullable(fechaHoraRegistro);
    }

    public InstantFilter fechaHoraRegistro() {
        if (fechaHoraRegistro == null) {
            setFechaHoraRegistro(new InstantFilter());
        }
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(InstantFilter fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public BigDecimalFilter getPeso() {
        return peso;
    }

    public Optional<BigDecimalFilter> optionalPeso() {
        return Optional.ofNullable(peso);
    }

    public BigDecimalFilter peso() {
        if (peso == null) {
            setPeso(new BigDecimalFilter());
        }
        return peso;
    }

    public void setPeso(BigDecimalFilter peso) {
        this.peso = peso;
    }

    public BigDecimalFilter getTalla() {
        return talla;
    }

    public Optional<BigDecimalFilter> optionalTalla() {
        return Optional.ofNullable(talla);
    }

    public BigDecimalFilter talla() {
        if (talla == null) {
            setTalla(new BigDecimalFilter());
        }
        return talla;
    }

    public void setTalla(BigDecimalFilter talla) {
        this.talla = talla;
    }

    public BigDecimalFilter getTemperatura() {
        return temperatura;
    }

    public Optional<BigDecimalFilter> optionalTemperatura() {
        return Optional.ofNullable(temperatura);
    }

    public BigDecimalFilter temperatura() {
        if (temperatura == null) {
            setTemperatura(new BigDecimalFilter());
        }
        return temperatura;
    }

    public void setTemperatura(BigDecimalFilter temperatura) {
        this.temperatura = temperatura;
    }

    public StringFilter getPresionArterial() {
        return presionArterial;
    }

    public Optional<StringFilter> optionalPresionArterial() {
        return Optional.ofNullable(presionArterial);
    }

    public StringFilter presionArterial() {
        if (presionArterial == null) {
            setPresionArterial(new StringFilter());
        }
        return presionArterial;
    }

    public void setPresionArterial(StringFilter presionArterial) {
        this.presionArterial = presionArterial;
    }

    public IntegerFilter getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public Optional<IntegerFilter> optionalFrecuenciaCardiaca() {
        return Optional.ofNullable(frecuenciaCardiaca);
    }

    public IntegerFilter frecuenciaCardiaca() {
        if (frecuenciaCardiaca == null) {
            setFrecuenciaCardiaca(new IntegerFilter());
        }
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(IntegerFilter frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public IntegerFilter getFrecuenciaRespiratoria() {
        return frecuenciaRespiratoria;
    }

    public Optional<IntegerFilter> optionalFrecuenciaRespiratoria() {
        return Optional.ofNullable(frecuenciaRespiratoria);
    }

    public IntegerFilter frecuenciaRespiratoria() {
        if (frecuenciaRespiratoria == null) {
            setFrecuenciaRespiratoria(new IntegerFilter());
        }
        return frecuenciaRespiratoria;
    }

    public void setFrecuenciaRespiratoria(IntegerFilter frecuenciaRespiratoria) {
        this.frecuenciaRespiratoria = frecuenciaRespiratoria;
    }

    public IntegerFilter getSaturacionOxigeno() {
        return saturacionOxigeno;
    }

    public Optional<IntegerFilter> optionalSaturacionOxigeno() {
        return Optional.ofNullable(saturacionOxigeno);
    }

    public IntegerFilter saturacionOxigeno() {
        if (saturacionOxigeno == null) {
            setSaturacionOxigeno(new IntegerFilter());
        }
        return saturacionOxigeno;
    }

    public void setSaturacionOxigeno(IntegerFilter saturacionOxigeno) {
        this.saturacionOxigeno = saturacionOxigeno;
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

    public LongFilter getConsultaId() {
        return consultaId;
    }

    public Optional<LongFilter> optionalConsultaId() {
        return Optional.ofNullable(consultaId);
    }

    public LongFilter consultaId() {
        if (consultaId == null) {
            setConsultaId(new LongFilter());
        }
        return consultaId;
    }

    public void setConsultaId(LongFilter consultaId) {
        this.consultaId = consultaId;
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
        final SignosVitalesCriteria that = (SignosVitalesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaHoraRegistro, that.fechaHoraRegistro) &&
            Objects.equals(peso, that.peso) &&
            Objects.equals(talla, that.talla) &&
            Objects.equals(temperatura, that.temperatura) &&
            Objects.equals(presionArterial, that.presionArterial) &&
            Objects.equals(frecuenciaCardiaca, that.frecuenciaCardiaca) &&
            Objects.equals(frecuenciaRespiratoria, that.frecuenciaRespiratoria) &&
            Objects.equals(saturacionOxigeno, that.saturacionOxigeno) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(consultaId, that.consultaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fechaHoraRegistro,
            peso,
            talla,
            temperatura,
            presionArterial,
            frecuenciaCardiaca,
            frecuenciaRespiratoria,
            saturacionOxigeno,
            activo,
            fechaAlta,
            fechaBaja,
            consultaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SignosVitalesCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFechaHoraRegistro().map(f -> "fechaHoraRegistro=" + f + ", ").orElse("") +
            optionalPeso().map(f -> "peso=" + f + ", ").orElse("") +
            optionalTalla().map(f -> "talla=" + f + ", ").orElse("") +
            optionalTemperatura().map(f -> "temperatura=" + f + ", ").orElse("") +
            optionalPresionArterial().map(f -> "presionArterial=" + f + ", ").orElse("") +
            optionalFrecuenciaCardiaca().map(f -> "frecuenciaCardiaca=" + f + ", ").orElse("") +
            optionalFrecuenciaRespiratoria().map(f -> "frecuenciaRespiratoria=" + f + ", ").orElse("") +
            optionalSaturacionOxigeno().map(f -> "saturacionOxigeno=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalConsultaId().map(f -> "consultaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
