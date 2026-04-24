package com.mycompany.hospital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paciente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 40)
    @Column(name = "numero_historia_clinica", length = 40, nullable = false, unique = true)
    private String numeroHistoriaClinica;

    @Lob
    @Column(name = "alergias_generales")
    private String alergiasGenerales;

    @Lob
    @Column(name = "observaciones")
    private String observaciones;

    @NotNull
    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @JsonIgnoreProperties(value = { "tipoDocumento", "sexo", "ciudad", "paciente", "empleado" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    private ObraSocial obraSocial;

    @ManyToOne(fetch = FetchType.LAZY)
    private GrupoSanguineo grupoSanguineo;

    @ManyToOne(fetch = FetchType.LAZY)
    private FactorRh factorRh;

    @JsonIgnoreProperties(value = { "paciente" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "paciente")
    private HistoriaClinica historiaClinica;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paciente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroHistoriaClinica() {
        return this.numeroHistoriaClinica;
    }

    public Paciente numeroHistoriaClinica(String numeroHistoriaClinica) {
        this.setNumeroHistoriaClinica(numeroHistoriaClinica);
        return this;
    }

    public void setNumeroHistoriaClinica(String numeroHistoriaClinica) {
        this.numeroHistoriaClinica = numeroHistoriaClinica;
    }

    public String getAlergiasGenerales() {
        return this.alergiasGenerales;
    }

    public Paciente alergiasGenerales(String alergiasGenerales) {
        this.setAlergiasGenerales(alergiasGenerales);
        return this;
    }

    public void setAlergiasGenerales(String alergiasGenerales) {
        this.alergiasGenerales = alergiasGenerales;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Paciente observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDate getFechaAlta() {
        return this.fechaAlta;
    }

    public Paciente fechaAlta(LocalDate fechaAlta) {
        this.setFechaAlta(fechaAlta);
        return this;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return this.fechaBaja;
    }

    public Paciente fechaBaja(LocalDate fechaBaja) {
        this.setFechaBaja(fechaBaja);
        return this;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Paciente activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Persona getPersona() {
        return this.persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Paciente persona(Persona persona) {
        this.setPersona(persona);
        return this;
    }

    public ObraSocial getObraSocial() {
        return this.obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }

    public Paciente obraSocial(ObraSocial obraSocial) {
        this.setObraSocial(obraSocial);
        return this;
    }

    public GrupoSanguineo getGrupoSanguineo() {
        return this.grupoSanguineo;
    }

    public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public Paciente grupoSanguineo(GrupoSanguineo grupoSanguineo) {
        this.setGrupoSanguineo(grupoSanguineo);
        return this;
    }

    public FactorRh getFactorRh() {
        return this.factorRh;
    }

    public void setFactorRh(FactorRh factorRh) {
        this.factorRh = factorRh;
    }

    public Paciente factorRh(FactorRh factorRh) {
        this.setFactorRh(factorRh);
        return this;
    }

    public HistoriaClinica getHistoriaClinica() {
        return this.historiaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        if (this.historiaClinica != null) {
            this.historiaClinica.setPaciente(null);
        }
        if (historiaClinica != null) {
            historiaClinica.setPaciente(this);
        }
        this.historiaClinica = historiaClinica;
    }

    public Paciente historiaClinica(HistoriaClinica historiaClinica) {
        this.setHistoriaClinica(historiaClinica);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        return getId() != null && getId().equals(((Paciente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", numeroHistoriaClinica='" + getNumeroHistoriaClinica() + "'" +
            ", alergiasGenerales='" + getAlergiasGenerales() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", fechaAlta='" + getFechaAlta() + "'" +
            ", fechaBaja='" + getFechaBaja() + "'" +
            ", activo='" + getActivo() + "'" +
            "}";
    }
}
