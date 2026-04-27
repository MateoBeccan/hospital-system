package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.Turno} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.TurnoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /turnos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TurnoCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codigo;

    private InstantFilter fechaHora;

    private IntegerFilter duracionMinutos;

    private StringFilter motivoConsulta;

    private InstantFilter fechaCreacion;

    private BooleanFilter activo;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private LongFilter pacienteId;

    private LongFilter medicoId;

    private LongFilter especialidadId;

    private LongFilter estadoTurnoId;

    private LongFilter canalSolicitudId;

    private LongFilter consultaId;

    private Boolean distinct;

    public TurnoCriteria() {}

    public TurnoCriteria(TurnoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.codigo = other.optionalCodigo().map(StringFilter::copy).orElse(null);
        this.fechaHora = other.optionalFechaHora().map(InstantFilter::copy).orElse(null);
        this.duracionMinutos = other.optionalDuracionMinutos().map(IntegerFilter::copy).orElse(null);
        this.motivoConsulta = other.optionalMotivoConsulta().map(StringFilter::copy).orElse(null);
        this.fechaCreacion = other.optionalFechaCreacion().map(InstantFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.pacienteId = other.optionalPacienteId().map(LongFilter::copy).orElse(null);
        this.medicoId = other.optionalMedicoId().map(LongFilter::copy).orElse(null);
        this.especialidadId = other.optionalEspecialidadId().map(LongFilter::copy).orElse(null);
        this.estadoTurnoId = other.optionalEstadoTurnoId().map(LongFilter::copy).orElse(null);
        this.canalSolicitudId = other.optionalCanalSolicitudId().map(LongFilter::copy).orElse(null);
        this.consultaId = other.optionalConsultaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TurnoCriteria copy() {
        return new TurnoCriteria(this);
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

    public InstantFilter getFechaHora() {
        return fechaHora;
    }

    public Optional<InstantFilter> optionalFechaHora() {
        return Optional.ofNullable(fechaHora);
    }

    public InstantFilter fechaHora() {
        if (fechaHora == null) {
            setFechaHora(new InstantFilter());
        }
        return fechaHora;
    }

    public void setFechaHora(InstantFilter fechaHora) {
        this.fechaHora = fechaHora;
    }

    public IntegerFilter getDuracionMinutos() {
        return duracionMinutos;
    }

    public Optional<IntegerFilter> optionalDuracionMinutos() {
        return Optional.ofNullable(duracionMinutos);
    }

    public IntegerFilter duracionMinutos() {
        if (duracionMinutos == null) {
            setDuracionMinutos(new IntegerFilter());
        }
        return duracionMinutos;
    }

    public void setDuracionMinutos(IntegerFilter duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
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

    public InstantFilter getFechaCreacion() {
        return fechaCreacion;
    }

    public Optional<InstantFilter> optionalFechaCreacion() {
        return Optional.ofNullable(fechaCreacion);
    }

    public InstantFilter fechaCreacion() {
        if (fechaCreacion == null) {
            setFechaCreacion(new InstantFilter());
        }
        return fechaCreacion;
    }

    public void setFechaCreacion(InstantFilter fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public LongFilter getEspecialidadId() {
        return especialidadId;
    }

    public Optional<LongFilter> optionalEspecialidadId() {
        return Optional.ofNullable(especialidadId);
    }

    public LongFilter especialidadId() {
        if (especialidadId == null) {
            setEspecialidadId(new LongFilter());
        }
        return especialidadId;
    }

    public void setEspecialidadId(LongFilter especialidadId) {
        this.especialidadId = especialidadId;
    }

    public LongFilter getEstadoTurnoId() {
        return estadoTurnoId;
    }

    public Optional<LongFilter> optionalEstadoTurnoId() {
        return Optional.ofNullable(estadoTurnoId);
    }

    public LongFilter estadoTurnoId() {
        if (estadoTurnoId == null) {
            setEstadoTurnoId(new LongFilter());
        }
        return estadoTurnoId;
    }

    public void setEstadoTurnoId(LongFilter estadoTurnoId) {
        this.estadoTurnoId = estadoTurnoId;
    }

    public LongFilter getCanalSolicitudId() {
        return canalSolicitudId;
    }

    public Optional<LongFilter> optionalCanalSolicitudId() {
        return Optional.ofNullable(canalSolicitudId);
    }

    public LongFilter canalSolicitudId() {
        if (canalSolicitudId == null) {
            setCanalSolicitudId(new LongFilter());
        }
        return canalSolicitudId;
    }

    public void setCanalSolicitudId(LongFilter canalSolicitudId) {
        this.canalSolicitudId = canalSolicitudId;
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
        final TurnoCriteria that = (TurnoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(fechaHora, that.fechaHora) &&
            Objects.equals(duracionMinutos, that.duracionMinutos) &&
            Objects.equals(motivoConsulta, that.motivoConsulta) &&
            Objects.equals(fechaCreacion, that.fechaCreacion) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(pacienteId, that.pacienteId) &&
            Objects.equals(medicoId, that.medicoId) &&
            Objects.equals(especialidadId, that.especialidadId) &&
            Objects.equals(estadoTurnoId, that.estadoTurnoId) &&
            Objects.equals(canalSolicitudId, that.canalSolicitudId) &&
            Objects.equals(consultaId, that.consultaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codigo,
            fechaHora,
            duracionMinutos,
            motivoConsulta,
            fechaCreacion,
            activo,
            fechaAlta,
            fechaBaja,
            pacienteId,
            medicoId,
            especialidadId,
            estadoTurnoId,
            canalSolicitudId,
            consultaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TurnoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCodigo().map(f -> "codigo=" + f + ", ").orElse("") +
            optionalFechaHora().map(f -> "fechaHora=" + f + ", ").orElse("") +
            optionalDuracionMinutos().map(f -> "duracionMinutos=" + f + ", ").orElse("") +
            optionalMotivoConsulta().map(f -> "motivoConsulta=" + f + ", ").orElse("") +
            optionalFechaCreacion().map(f -> "fechaCreacion=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalPacienteId().map(f -> "pacienteId=" + f + ", ").orElse("") +
            optionalMedicoId().map(f -> "medicoId=" + f + ", ").orElse("") +
            optionalEspecialidadId().map(f -> "especialidadId=" + f + ", ").orElse("") +
            optionalEstadoTurnoId().map(f -> "estadoTurnoId=" + f + ", ").orElse("") +
            optionalCanalSolicitudId().map(f -> "canalSolicitudId=" + f + ", ").orElse("") +
            optionalConsultaId().map(f -> "consultaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
