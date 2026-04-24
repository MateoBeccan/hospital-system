package com.mycompany.hospital.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.hospital.domain.Persona} entity. This class is used
 * in {@link com.mycompany.hospital.web.rest.PersonaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /personas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonaCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter apellido;

    private StringFilter nroDocumento;

    private LocalDateFilter fechaNacimiento;

    private StringFilter telefono;

    private StringFilter email;

    private StringFilter direccion;

    private BooleanFilter activo;

    private LocalDateFilter fechaAlta;

    private LocalDateFilter fechaBaja;

    private LongFilter tipoDocumentoId;

    private LongFilter sexoId;

    private LongFilter ciudadId;

    private LongFilter pacienteId;

    private LongFilter empleadoId;

    private Boolean distinct;

    public PersonaCriteria() {}

    public PersonaCriteria(PersonaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.apellido = other.optionalApellido().map(StringFilter::copy).orElse(null);
        this.nroDocumento = other.optionalNroDocumento().map(StringFilter::copy).orElse(null);
        this.fechaNacimiento = other.optionalFechaNacimiento().map(LocalDateFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.direccion = other.optionalDireccion().map(StringFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.fechaAlta = other.optionalFechaAlta().map(LocalDateFilter::copy).orElse(null);
        this.fechaBaja = other.optionalFechaBaja().map(LocalDateFilter::copy).orElse(null);
        this.tipoDocumentoId = other.optionalTipoDocumentoId().map(LongFilter::copy).orElse(null);
        this.sexoId = other.optionalSexoId().map(LongFilter::copy).orElse(null);
        this.ciudadId = other.optionalCiudadId().map(LongFilter::copy).orElse(null);
        this.pacienteId = other.optionalPacienteId().map(LongFilter::copy).orElse(null);
        this.empleadoId = other.optionalEmpleadoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PersonaCriteria copy() {
        return new PersonaCriteria(this);
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

    public StringFilter getApellido() {
        return apellido;
    }

    public Optional<StringFilter> optionalApellido() {
        return Optional.ofNullable(apellido);
    }

    public StringFilter apellido() {
        if (apellido == null) {
            setApellido(new StringFilter());
        }
        return apellido;
    }

    public void setApellido(StringFilter apellido) {
        this.apellido = apellido;
    }

    public StringFilter getNroDocumento() {
        return nroDocumento;
    }

    public Optional<StringFilter> optionalNroDocumento() {
        return Optional.ofNullable(nroDocumento);
    }

    public StringFilter nroDocumento() {
        if (nroDocumento == null) {
            setNroDocumento(new StringFilter());
        }
        return nroDocumento;
    }

    public void setNroDocumento(StringFilter nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public LocalDateFilter getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Optional<LocalDateFilter> optionalFechaNacimiento() {
        return Optional.ofNullable(fechaNacimiento);
    }

    public LocalDateFilter fechaNacimiento() {
        if (fechaNacimiento == null) {
            setFechaNacimiento(new LocalDateFilter());
        }
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDateFilter fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public Optional<StringFilter> optionalTelefono() {
        return Optional.ofNullable(telefono);
    }

    public StringFilter telefono() {
        if (telefono == null) {
            setTelefono(new StringFilter());
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getDireccion() {
        return direccion;
    }

    public Optional<StringFilter> optionalDireccion() {
        return Optional.ofNullable(direccion);
    }

    public StringFilter direccion() {
        if (direccion == null) {
            setDireccion(new StringFilter());
        }
        return direccion;
    }

    public void setDireccion(StringFilter direccion) {
        this.direccion = direccion;
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

    public LongFilter getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public Optional<LongFilter> optionalTipoDocumentoId() {
        return Optional.ofNullable(tipoDocumentoId);
    }

    public LongFilter tipoDocumentoId() {
        if (tipoDocumentoId == null) {
            setTipoDocumentoId(new LongFilter());
        }
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(LongFilter tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public LongFilter getSexoId() {
        return sexoId;
    }

    public Optional<LongFilter> optionalSexoId() {
        return Optional.ofNullable(sexoId);
    }

    public LongFilter sexoId() {
        if (sexoId == null) {
            setSexoId(new LongFilter());
        }
        return sexoId;
    }

    public void setSexoId(LongFilter sexoId) {
        this.sexoId = sexoId;
    }

    public LongFilter getCiudadId() {
        return ciudadId;
    }

    public Optional<LongFilter> optionalCiudadId() {
        return Optional.ofNullable(ciudadId);
    }

    public LongFilter ciudadId() {
        if (ciudadId == null) {
            setCiudadId(new LongFilter());
        }
        return ciudadId;
    }

    public void setCiudadId(LongFilter ciudadId) {
        this.ciudadId = ciudadId;
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

    public LongFilter getEmpleadoId() {
        return empleadoId;
    }

    public Optional<LongFilter> optionalEmpleadoId() {
        return Optional.ofNullable(empleadoId);
    }

    public LongFilter empleadoId() {
        if (empleadoId == null) {
            setEmpleadoId(new LongFilter());
        }
        return empleadoId;
    }

    public void setEmpleadoId(LongFilter empleadoId) {
        this.empleadoId = empleadoId;
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
        final PersonaCriteria that = (PersonaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(apellido, that.apellido) &&
            Objects.equals(nroDocumento, that.nroDocumento) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(email, that.email) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(fechaAlta, that.fechaAlta) &&
            Objects.equals(fechaBaja, that.fechaBaja) &&
            Objects.equals(tipoDocumentoId, that.tipoDocumentoId) &&
            Objects.equals(sexoId, that.sexoId) &&
            Objects.equals(ciudadId, that.ciudadId) &&
            Objects.equals(pacienteId, that.pacienteId) &&
            Objects.equals(empleadoId, that.empleadoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            apellido,
            nroDocumento,
            fechaNacimiento,
            telefono,
            email,
            direccion,
            activo,
            fechaAlta,
            fechaBaja,
            tipoDocumentoId,
            sexoId,
            ciudadId,
            pacienteId,
            empleadoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalApellido().map(f -> "apellido=" + f + ", ").orElse("") +
            optionalNroDocumento().map(f -> "nroDocumento=" + f + ", ").orElse("") +
            optionalFechaNacimiento().map(f -> "fechaNacimiento=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalDireccion().map(f -> "direccion=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalFechaAlta().map(f -> "fechaAlta=" + f + ", ").orElse("") +
            optionalFechaBaja().map(f -> "fechaBaja=" + f + ", ").orElse("") +
            optionalTipoDocumentoId().map(f -> "tipoDocumentoId=" + f + ", ").orElse("") +
            optionalSexoId().map(f -> "sexoId=" + f + ", ").orElse("") +
            optionalCiudadId().map(f -> "ciudadId=" + f + ", ").orElse("") +
            optionalPacienteId().map(f -> "pacienteId=" + f + ", ").orElse("") +
            optionalEmpleadoId().map(f -> "empleadoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
