package com.mycompany.hospital.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A HistoriaClinica.
 */
@Entity
@Table(name = "historia_clinica")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriaClinica implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 40)
    @Column(name = "numero", length = 40, nullable = false, unique = true)
    private String numero;

    @NotNull
    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDate fechaUltimaActualizacion;

    @Lob
    @Column(name = "antecedentes_personales")
    private String antecedentesPersonales;

    @Lob
    @Column(name = "antecedentes_familiares")
    private String antecedentesFamiliares;

    @Lob
    @Column(name = "enfermedades_previas")
    private String enfermedadesPrevias;

    @Lob
    @Column(name = "cirugias_previas")
    private String cirugiasPrevias;

    @Lob
    @Column(name = "alergias")
    private String alergias;

    @Lob
    @Column(name = "medicacion_habitual")
    private String medicacionHabitual;

    @Lob
    @Column(name = "habitos")
    private String habitos;

    @Lob
    @Column(name = "observaciones_generales")
    private String observacionesGenerales;

    @NotNull
    @Column(name = "activa", nullable = false)
    private Boolean activa;

    @Column(name = "fecha_cierre")
    private LocalDate fechaCierre;

    @Size(max = 255)
    @Column(name = "motivo_cierre", length = 255)
    private String motivoCierre;

    @JsonIgnoreProperties(value = { "persona", "obraSocial", "grupoSanguineo", "factorRh", "historiaClinica" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Paciente paciente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoriaClinica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public HistoriaClinica numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFechaApertura() {
        return this.fechaApertura;
    }

    public HistoriaClinica fechaApertura(LocalDate fechaApertura) {
        this.setFechaApertura(fechaApertura);
        return this;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDate getFechaUltimaActualizacion() {
        return this.fechaUltimaActualizacion;
    }

    public HistoriaClinica fechaUltimaActualizacion(LocalDate fechaUltimaActualizacion) {
        this.setFechaUltimaActualizacion(fechaUltimaActualizacion);
        return this;
    }

    public void setFechaUltimaActualizacion(LocalDate fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    public String getAntecedentesPersonales() {
        return this.antecedentesPersonales;
    }

    public HistoriaClinica antecedentesPersonales(String antecedentesPersonales) {
        this.setAntecedentesPersonales(antecedentesPersonales);
        return this;
    }

    public void setAntecedentesPersonales(String antecedentesPersonales) {
        this.antecedentesPersonales = antecedentesPersonales;
    }

    public String getAntecedentesFamiliares() {
        return this.antecedentesFamiliares;
    }

    public HistoriaClinica antecedentesFamiliares(String antecedentesFamiliares) {
        this.setAntecedentesFamiliares(antecedentesFamiliares);
        return this;
    }

    public void setAntecedentesFamiliares(String antecedentesFamiliares) {
        this.antecedentesFamiliares = antecedentesFamiliares;
    }

    public String getEnfermedadesPrevias() {
        return this.enfermedadesPrevias;
    }

    public HistoriaClinica enfermedadesPrevias(String enfermedadesPrevias) {
        this.setEnfermedadesPrevias(enfermedadesPrevias);
        return this;
    }

    public void setEnfermedadesPrevias(String enfermedadesPrevias) {
        this.enfermedadesPrevias = enfermedadesPrevias;
    }

    public String getCirugiasPrevias() {
        return this.cirugiasPrevias;
    }

    public HistoriaClinica cirugiasPrevias(String cirugiasPrevias) {
        this.setCirugiasPrevias(cirugiasPrevias);
        return this;
    }

    public void setCirugiasPrevias(String cirugiasPrevias) {
        this.cirugiasPrevias = cirugiasPrevias;
    }

    public String getAlergias() {
        return this.alergias;
    }

    public HistoriaClinica alergias(String alergias) {
        this.setAlergias(alergias);
        return this;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getMedicacionHabitual() {
        return this.medicacionHabitual;
    }

    public HistoriaClinica medicacionHabitual(String medicacionHabitual) {
        this.setMedicacionHabitual(medicacionHabitual);
        return this;
    }

    public void setMedicacionHabitual(String medicacionHabitual) {
        this.medicacionHabitual = medicacionHabitual;
    }

    public String getHabitos() {
        return this.habitos;
    }

    public HistoriaClinica habitos(String habitos) {
        this.setHabitos(habitos);
        return this;
    }

    public void setHabitos(String habitos) {
        this.habitos = habitos;
    }

    public String getObservacionesGenerales() {
        return this.observacionesGenerales;
    }

    public HistoriaClinica observacionesGenerales(String observacionesGenerales) {
        this.setObservacionesGenerales(observacionesGenerales);
        return this;
    }

    public void setObservacionesGenerales(String observacionesGenerales) {
        this.observacionesGenerales = observacionesGenerales;
    }

    public Boolean getActiva() {
        return this.activa;
    }

    public HistoriaClinica activa(Boolean activa) {
        this.setActiva(activa);
        return this;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public LocalDate getFechaCierre() {
        return this.fechaCierre;
    }

    public HistoriaClinica fechaCierre(LocalDate fechaCierre) {
        this.setFechaCierre(fechaCierre);
        return this;
    }

    public void setFechaCierre(LocalDate fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getMotivoCierre() {
        return this.motivoCierre;
    }

    public HistoriaClinica motivoCierre(String motivoCierre) {
        this.setMotivoCierre(motivoCierre);
        return this;
    }

    public void setMotivoCierre(String motivoCierre) {
        this.motivoCierre = motivoCierre;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public HistoriaClinica paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriaClinica)) {
            return false;
        }
        return getId() != null && getId().equals(((HistoriaClinica) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriaClinica{" +
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
            "}";
    }
}
