package com.mycompany.hospital.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.hospital.domain.HistoriaClinica} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriaClinicaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 40)
    private String numero;

    @NotNull
    private LocalDate fechaApertura;

    private LocalDate fechaUltimaActualizacion;

    @Lob
    private String antecedentesPersonales;

    @Lob
    private String antecedentesFamiliares;

    @Lob
    private String enfermedadesPrevias;

    @Lob
    private String cirugiasPrevias;

    @Lob
    private String alergias;

    @Lob
    private String medicacionHabitual;

    @Lob
    private String habitos;

    @Lob
    private String observacionesGenerales;

    @NotNull
    private Boolean activa;

    private LocalDate fechaCierre;

    @Size(max = 255)
    private String motivoCierre;

    @NotNull
    private PacienteDTO paciente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDate getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(LocalDate fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    public String getAntecedentesPersonales() {
        return antecedentesPersonales;
    }

    public void setAntecedentesPersonales(String antecedentesPersonales) {
        this.antecedentesPersonales = antecedentesPersonales;
    }

    public String getAntecedentesFamiliares() {
        return antecedentesFamiliares;
    }

    public void setAntecedentesFamiliares(String antecedentesFamiliares) {
        this.antecedentesFamiliares = antecedentesFamiliares;
    }

    public String getEnfermedadesPrevias() {
        return enfermedadesPrevias;
    }

    public void setEnfermedadesPrevias(String enfermedadesPrevias) {
        this.enfermedadesPrevias = enfermedadesPrevias;
    }

    public String getCirugiasPrevias() {
        return cirugiasPrevias;
    }

    public void setCirugiasPrevias(String cirugiasPrevias) {
        this.cirugiasPrevias = cirugiasPrevias;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getMedicacionHabitual() {
        return medicacionHabitual;
    }

    public void setMedicacionHabitual(String medicacionHabitual) {
        this.medicacionHabitual = medicacionHabitual;
    }

    public String getHabitos() {
        return habitos;
    }

    public void setHabitos(String habitos) {
        this.habitos = habitos;
    }

    public String getObservacionesGenerales() {
        return observacionesGenerales;
    }

    public void setObservacionesGenerales(String observacionesGenerales) {
        this.observacionesGenerales = observacionesGenerales;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public LocalDate getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDate fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getMotivoCierre() {
        return motivoCierre;
    }

    public void setMotivoCierre(String motivoCierre) {
        this.motivoCierre = motivoCierre;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriaClinicaDTO)) {
            return false;
        }

        HistoriaClinicaDTO historiaClinicaDTO = (HistoriaClinicaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiaClinicaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriaClinicaDTO{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", fechaApertura='" + getFechaApertura() + "'" +
            ", fechaUltimaActualizacion='" + getFechaUltimaActualizacion() + "'" +
            ", antecedentesPersonales='" + getAntecedentesPersonales() + "'" +
            ", antecedentesFamiliares='" + getAntecedentesFamiliares() + "'" +
            ", enfermedadesPrevias='" + getEnfermedadesPrevias() + "'" +
            ", cirugiasPrevias='" + getCirugiasPrevias() + "'" +
            ", alergias='" + getAlergias() + "'" +
            ", medicacionHabitual='" + getMedicacionHabitual() + "'" +
            ", habitos='" + getHabitos() + "'" +
            ", observacionesGenerales='" + getObservacionesGenerales() + "'" +
            ", activa='" + getActiva() + "'" +
            ", fechaCierre='" + getFechaCierre() + "'" +
            ", motivoCierre='" + getMotivoCierre() + "'" +
            ", paciente=" + getPaciente() +
            "}";
    }
}
