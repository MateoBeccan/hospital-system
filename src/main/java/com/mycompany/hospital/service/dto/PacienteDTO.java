package com.mycompany.hospital.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.hospital.domain.Paciente} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 40)
    private String numeroHistoriaClinica;

    @Lob
    private String alergiasGenerales;

    @Lob
    private String observaciones;

    @NotNull
    private LocalDate fechaAlta;

    private LocalDate fechaBaja;

    @NotNull
    private Boolean activo;

    @NotNull
    private PersonaDTO persona;

    private ObraSocialDTO obraSocial;

    private GrupoSanguineoDTO grupoSanguineo;

    private FactorRhDTO factorRh;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroHistoriaClinica() {
        return numeroHistoriaClinica;
    }

    public void setNumeroHistoriaClinica(String numeroHistoriaClinica) {
        this.numeroHistoriaClinica = numeroHistoriaClinica;
    }

    public String getAlergiasGenerales() {
        return alergiasGenerales;
    }

    public void setAlergiasGenerales(String alergiasGenerales) {
        this.alergiasGenerales = alergiasGenerales;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public PersonaDTO getPersona() {
        return persona;
    }

    public void setPersona(PersonaDTO persona) {
        this.persona = persona;
    }

    public ObraSocialDTO getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocialDTO obraSocial) {
        this.obraSocial = obraSocial;
    }

    public GrupoSanguineoDTO getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(GrupoSanguineoDTO grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public FactorRhDTO getFactorRh() {
        return factorRh;
    }

    public void setFactorRh(FactorRhDTO factorRh) {
        this.factorRh = factorRh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacienteDTO)) {
            return false;
        }

        PacienteDTO pacienteDTO = (PacienteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pacienteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteDTO{" +
            "id=" + getId() +
            ", numeroHistoriaClinica='" + getNumeroHistoriaClinica() + "'" +
            ", alergiasGenerales='" + getAlergiasGenerales() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            ", activo='" + getActivo() + "'" +
            ", persona=" + getPersona() +
            ", obraSocial=" + getObraSocial() +
            ", grupoSanguineo=" + getGrupoSanguineo() +
            ", factorRh=" + getFactorRh() +
            "}";
    }
}
