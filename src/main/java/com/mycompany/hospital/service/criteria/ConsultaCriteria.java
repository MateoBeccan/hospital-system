package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.Consulta} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.ConsultaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /consultas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsultaCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codigo;

    private InstantFilter fechaHoraInicio;

    private InstantFilter fechaHoraFin;

    private StringFilter motivoConsulta;

    private BooleanFilter activa;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private LongFilter turnoId;

    private LongFilter pacienteId;

    private LongFilter medicoId;

    private LongFilter historiaClinicaId;

    private Boolean distinct;

    public ConsultaCriteria() {}

    public ConsultaCriteria(ConsultaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.codigo = other.optionalCodigo().map(StringFilter::copy).orElse(null);
        this.fechaHoraInicio = other.optionalFechaHoraInicio().map(InstantFilter::copy).orElse(null);
        this.fechaHoraFin = other.optionalFechaHoraFin().map(InstantFilter::copy).orElse(null);
        this.motivoConsulta = other.optionalMotivoConsulta().map(StringFilter::copy).orElse(null);
        this.activa = other.optionalActiva().map(BooleanFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.turnoId = other.optionalTurnoId().map(LongFilter::copy).orElse(null);
        this.pacienteId = other.optionalPacienteId().map(LongFilter::copy).orElse(null);
        this.medicoId = other.optionalMedicoId().map(LongFilter::copy).orElse(null);
        this.historiaClinicaId = other.optionalHistoriaClinicaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ConsultaCriteria copy() {
        return new ConsultaCriteria(this);
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

    public InstantFilter getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public Optional<InstantFilter> optionalFechaHoraInicio() {
        return Optional.ofNullable(fechaHoraInicio);
    }

    public InstantFilter fechaHoraInicio() {
        if (fechaHoraInicio == null) {
            setFechaHoraInicio(new InstantFilter());
        }
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(InstantFilter fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public InstantFilter getFechaHoraFin() {
        return fechaHoraFin;
    }

    public Optional<InstantFilter> optionalFechaHoraFin() {
        return Optional.ofNullable(fechaHoraFin);
    }

    public InstantFilter fechaHoraFin() {
        if (fechaHoraFin == null) {
            setFechaHoraFin(new InstantFilter());
        }
        return fechaHoraFin;
    }

    public void setFechaHoraFin(InstantFilter fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public StringFilter getMotivoConsulta() {
        return motivoConsulta;
    }

    public Optional<StringFilter> optionalMotivoConsulta() {
        return Optional.ofNullable(motivoConsulta);
    }

    public StringFilter motivoConsulta() {
        if (motivoConsulta == null) {
            setMotivoConsulta(new StringFilter());
        }
        return motivoConsulta;
    }

    public void setMotivoConsulta(StringFilter motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
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

    public LongFilter getTurnoId() {
        return turnoId;
    }

    public Optional<LongFilter> optionalTurnoId() {
        return Optional.ofNullable(turnoId);
    }

    public LongFilter turnoId() {
        if (turnoId == null) {
            setTurnoId(new LongFilter());
        }
        return turnoId;
    }

    public void setTurnoId(LongFilter turnoId) {
        this.turnoId = turnoId;
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
        final ConsultaCriteria that = (ConsultaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(fechaHoraInicio, that.fechaHoraInicio) &&
            Objects.equals(fechaHoraFin, that.fechaHoraFin) &&
            Objects.equals(motivoConsulta, that.motivoConsulta) &&
            Objects.equals(activa, that.activa) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(turnoId, that.turnoId) &&
            Objects.equals(pacienteId, that.pacienteId) &&
            Objects.equals(medicoId, that.medicoId) &&
            Objects.equals(historiaClinicaId, that.historiaClinicaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codigo,
            fechaHoraInicio,
            fechaHoraFin,
            motivoConsulta,
            activa,
            fechaAlta,
            fechaBaja,
            turnoId,
            pacienteId,
            medicoId,
            historiaClinicaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsultaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCodigo().map(f -> "codigo=" + f + ", ").orElse("") +
            optionalFechaHoraInicio().map(f -> "fechaHoraInicio=" + f + ", ").orElse("") +
            optionalFechaHoraFin().map(f -> "fechaHoraFin=" + f + ", ").orElse("") +
            optionalMotivoConsulta().map(f -> "motivoConsulta=" + f + ", ").orElse("") +
            optionalActiva().map(f -> "activa=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalTurnoId().map(f -> "turnoId=" + f + ", ").orElse("") +
            optionalPacienteId().map(f -> "pacienteId=" + f + ", ").orElse("") +
            optionalMedicoId().map(f -> "medicoId=" + f + ", ").orElse("") +
            optionalHistoriaClinicaId().map(f -> "historiaClinicaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
