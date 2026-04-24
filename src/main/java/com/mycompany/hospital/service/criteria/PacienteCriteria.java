package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.Paciente} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.PacienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pacientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numeroHistoriaClinica;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private BooleanFilter activo;

    private LongFilter personaId;

    private LongFilter obraSocialId;

    private LongFilter grupoSanguineoId;

    private LongFilter factorRhId;

    private LongFilter historiaClinicaId;

    private Boolean distinct;

    public PacienteCriteria() {}

    public PacienteCriteria(PacienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.numeroHistoriaClinica = other.optionalNumeroHistoriaClinica().map(StringFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.personaId = other.optionalPersonaId().map(LongFilter::copy).orElse(null);
        this.obraSocialId = other.optionalObraSocialId().map(LongFilter::copy).orElse(null);
        this.grupoSanguineoId = other.optionalGrupoSanguineoId().map(LongFilter::copy).orElse(null);
        this.factorRhId = other.optionalFactorRhId().map(LongFilter::copy).orElse(null);
        this.historiaClinicaId = other.optionalHistoriaClinicaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PacienteCriteria copy() {
        return new PacienteCriteria(this);
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

    public StringFilter getNumeroHistoriaClinica() {
        return numeroHistoriaClinica;
    }

    public Optional<StringFilter> optionalNumeroHistoriaClinica() {
        return Optional.ofNullable(numeroHistoriaClinica);
    }

    public StringFilter numeroHistoriaClinica() {
        if (numeroHistoriaClinica == null) {
            setNumeroHistoriaClinica(new StringFilter());
        }
        return numeroHistoriaClinica;
    }

    public void setNumeroHistoriaClinica(StringFilter numeroHistoriaClinica) {
        this.numeroHistoriaClinica = numeroHistoriaClinica;
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

    public LongFilter getObraSocialId() {
        return obraSocialId;
    }

    public Optional<LongFilter> optionalObraSocialId() {
        return Optional.ofNullable(obraSocialId);
    }

    public LongFilter obraSocialId() {
        if (obraSocialId == null) {
            setObraSocialId(new LongFilter());
        }
        return obraSocialId;
    }

    public void setObraSocialId(LongFilter obraSocialId) {
        this.obraSocialId = obraSocialId;
    }

    public LongFilter getGrupoSanguineoId() {
        return grupoSanguineoId;
    }

    public Optional<LongFilter> optionalGrupoSanguineoId() {
        return Optional.ofNullable(grupoSanguineoId);
    }

    public LongFilter grupoSanguineoId() {
        if (grupoSanguineoId == null) {
            setGrupoSanguineoId(new LongFilter());
        }
        return grupoSanguineoId;
    }

    public void setGrupoSanguineoId(LongFilter grupoSanguineoId) {
        this.grupoSanguineoId = grupoSanguineoId;
    }

    public LongFilter getFactorRhId() {
        return factorRhId;
    }

    public Optional<LongFilter> optionalFactorRhId() {
        return Optional.ofNullable(factorRhId);
    }

    public LongFilter factorRhId() {
        if (factorRhId == null) {
            setFactorRhId(new LongFilter());
        }
        return factorRhId;
    }

    public void setFactorRhId(LongFilter factorRhId) {
        this.factorRhId = factorRhId;
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
        final PacienteCriteria that = (PacienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numeroHistoriaClinica, that.numeroHistoriaClinica) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(personaId, that.personaId) &&
            Objects.equals(obraSocialId, that.obraSocialId) &&
            Objects.equals(grupoSanguineoId, that.grupoSanguineoId) &&
            Objects.equals(factorRhId, that.factorRhId) &&
            Objects.equals(historiaClinicaId, that.historiaClinicaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            numeroHistoriaClinica,
            fechaAlta,
            fechaBaja,
            activo,
            personaId,
            obraSocialId,
            grupoSanguineoId,
            factorRhId,
            historiaClinicaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNumeroHistoriaClinica().map(f -> "numeroHistoriaClinica=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalPersonaId().map(f -> "personaId=" + f + ", ").orElse("") +
            optionalObraSocialId().map(f -> "obraSocialId=" + f + ", ").orElse("") +
            optionalGrupoSanguineoId().map(f -> "grupoSanguineoId=" + f + ", ").orElse("") +
            optionalFactorRhId().map(f -> "factorRhId=" + f + ", ").orElse("") +
            optionalHistoriaClinicaId().map(f -> "historiaClinicaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
