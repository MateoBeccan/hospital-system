package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.FactorRhTestSamples.*;
import static com.mycompany.hospital.domain.GrupoSanguineoTestSamples.*;
import static com.mycompany.hospital.domain.HistoriaClinicaTestSamples.*;
import static com.mycompany.hospital.domain.ObraSocialTestSamples.*;
import static com.mycompany.hospital.domain.PacienteTestSamples.*;
import static com.mycompany.hospital.domain.PersonaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PacienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paciente.class);
        Paciente paciente1 = getPacienteSample1();
        Paciente paciente2 = new Paciente();
        assertThat(paciente1).isNotEqualTo(paciente2);

        paciente2.setId(paciente1.getId());
        assertThat(paciente1).isEqualTo(paciente2);

        paciente2 = getPacienteSample2();
        assertThat(paciente1).isNotEqualTo(paciente2);
    }

    @Test
    void personaTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Persona personaBack = getPersonaRandomSampleGenerator();

        paciente.setPersona(personaBack);
        assertThat(paciente.getPersona()).isEqualTo(personaBack);

        paciente.persona(null);
        assertThat(paciente.getPersona()).isNull();
    }

    @Test
    void obraSocialTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        ObraSocial obraSocialBack = getObraSocialRandomSampleGenerator();

        paciente.setObraSocial(obraSocialBack);
        assertThat(paciente.getObraSocial()).isEqualTo(obraSocialBack);

        paciente.obraSocial(null);
        assertThat(paciente.getObraSocial()).isNull();
    }

    @Test
    void grupoSanguineoTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        GrupoSanguineo grupoSanguineoBack = getGrupoSanguineoRandomSampleGenerator();

        paciente.setGrupoSanguineo(grupoSanguineoBack);
        assertThat(paciente.getGrupoSanguineo()).isEqualTo(grupoSanguineoBack);

        paciente.grupoSanguineo(null);
        assertThat(paciente.getGrupoSanguineo()).isNull();
    }

    @Test
    void factorRhTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        FactorRh factorRhBack = getFactorRhRandomSampleGenerator();

        paciente.setFactorRh(factorRhBack);
        assertThat(paciente.getFactorRh()).isEqualTo(factorRhBack);

        paciente.factorRh(null);
        assertThat(paciente.getFactorRh()).isNull();
    }

    @Test
    void historiaClinicaTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        HistoriaClinica historiaClinicaBack = getHistoriaClinicaRandomSampleGenerator();

        paciente.setHistoriaClinica(historiaClinicaBack);
        assertThat(paciente.getHistoriaClinica()).isEqualTo(historiaClinicaBack);
        assertThat(historiaClinicaBack.getPaciente()).isEqualTo(paciente);

        paciente.historiaClinica(null);
        assertThat(paciente.getHistoriaClinica()).isNull();
        assertThat(historiaClinicaBack.getPaciente()).isNull();
    }
}
