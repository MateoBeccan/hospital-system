package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.Diagnostico} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.DiagnosticoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /diagnosticos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DiagnosticoCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codigo;

    private LocalDateFilter fechaDiagnostico;

    private StringFilter descripcion;

    private BooleanFilter activo;

    private LocalDateFilter fechaResolucion;

    private BooleanFilter esPrincipal;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private LongFilter consultaId;

    private LongFilter pacienteId;

    private LongFilter medicoId;

    private LongFilter tipoDiagnosticoId;

    private LongFilter estadoDiagnosticoId;

    private Boolean distinct;

    public DiagnosticoCriteria() {}

    public DiagnosticoCriteria(DiagnosticoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.codigo = other.optionalCodigo().map(StringFilter::copy).orElse(null);
        this.fechaDiagnostico = other.optionalFechaDiagnostico().map(LocalDateFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.fechaResolucion = other.optionalFechaResolucion().map(LocalDateFilter::copy).orElse(null);
        this.esPrincipal = other.optionalEsPrincipal().map(BooleanFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.consultaId = other.optionalConsultaId().map(LongFilter::copy).orElse(null);
        this.pacienteId = other.optionalPacienteId().map(LongFilter::copy).orElse(null);
        this.medicoId = other.optionalMedicoId().map(LongFilter::copy).orElse(null);
        this.tipoDiagnosticoId = other.optionalTipoDiagnosticoId().map(LongFilter::copy).orElse(null);
        this.estadoDiagnosticoId = other.optionalEstadoDiagnosticoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DiagnosticoCriteria copy() {
        return new DiagnosticoCriteria(this);
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

    public LocalDateFilter getFechaDiagnostico() {
        return fechaDiagnostico;
    }

    public Optional<LocalDateFilter> optionalFechaDiagnostico() {
        return Optional.ofNullable(fechaDiagnostico);
    }

    public LocalDateFilter fechaDiagnostico() {
        if (fechaDiagnostico == null) {
            setFechaDiagnostico(new LocalDateFilter());
        }
        return fechaDiagnostico;
    }

    public void setFechaDiagnostico(LocalDateFilter fechaDiagnostico) {
        this.fechaDiagnostico = fechaDiagnostico;
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

    public LocalDateFilter getFechaResolucion() {
        return fechaResolucion;
    }

    public Optional<LocalDateFilter> optionalFechaResolucion() {
        return Optional.ofNullable(fechaResolucion);
    }

    public LocalDateFilter fechaResolucion() {
        if (fechaResolucion == null) {
            setFechaResolucion(new LocalDateFilter());
        }
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateFilter fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public BooleanFilter getEsPrincipal() {
        return esPrincipal;
    }

    public Optional<BooleanFilter> optionalEsPrincipal() {
        return Optional.ofNullable(esPrincipal);
    }

    public BooleanFilter esPrincipal() {
        if (esPrincipal == null) {
            setEsPrincipal(new BooleanFilter());
        }
        return esPrincipal;
    }

    public void setEsPrincipal(BooleanFilter esPrincipal) {
        this.esPrincipal = esPrincipal;
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

    public LongFilter getTipoDiagnosticoId() {
        return tipoDiagnosticoId;
    }

    public Optional<LongFilter> optionalTipoDiagnosticoId() {
        return Optional.ofNullable(tipoDiagnosticoId);
    }

    public LongFilter tipoDiagnosticoId() {
        if (tipoDiagnosticoId == null) {
            setTipoDiagnosticoId(new LongFilter());
        }
        return tipoDiagnosticoId;
    }

    public void setTipoDiagnosticoId(LongFilter tipoDiagnosticoId) {
        this.tipoDiagnosticoId = tipoDiagnosticoId;
    }

    public LongFilter getEstadoDiagnosticoId() {
        return estadoDiagnosticoId;
    }

    public Optional<LongFilter> optionalEstadoDiagnosticoId() {
        return Optional.ofNullable(estadoDiagnosticoId);
    }

    public LongFilter estadoDiagnosticoId() {
        if (estadoDiagnosticoId == null) {
            setEstadoDiagnosticoId(new LongFilter());
        }
        return estadoDiagnosticoId;
    }

    public void setEstadoDiagnosticoId(LongFilter estadoDiagnosticoId) {
        this.estadoDiagnosticoId = estadoDiagnosticoId;
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
        final DiagnosticoCriteria that = (DiagnosticoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(fechaDiagnostico, that.fechaDiagnostico) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(fechaResolucion, that.fechaResolucion) &&
            Objects.equals(esPrincipal, that.esPrincipal) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(consultaId, that.consultaId) &&
            Objects.equals(pacienteId, that.pacienteId) &&
            Objects.equals(medicoId, that.medicoId) &&
            Objects.equals(tipoDiagnosticoId, that.tipoDiagnosticoId) &&
            Objects.equals(estadoDiagnosticoId, that.estadoDiagnosticoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codigo,
            fechaDiagnostico,
            descripcion,
            activo,
            fechaResolucion,
            esPrincipal,
            fechaAlta,
            fechaBaja,
            consultaId,
            pacienteId,
            medicoId,
            tipoDiagnosticoId,
            estadoDiagnosticoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DiagnosticoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCodigo().map(f -> "codigo=" + f + ", ").orElse("") +
            optionalFechaDiagnostico().map(f -> "fechaDiagnostico=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalFechaResolucion().map(f -> "fechaResolucion=" + f + ", ").orElse("") +
            optionalEsPrincipal().map(f -> "esPrincipal=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalConsultaId().map(f -> "consultaId=" + f + ", ").orElse("") +
            optionalPacienteId().map(f -> "pacienteId=" + f + ", ").orElse("") +
            optionalMedicoId().map(f -> "medicoId=" + f + ", ").orElse("") +
            optionalTipoDiagnosticoId().map(f -> "tipoDiagnosticoId=" + f + ", ").orElse("") +
            optionalEstadoDiagnosticoId().map(f -> "estadoDiagnosticoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
