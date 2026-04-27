package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.ConsultaTestSamples.*;
import static com.mycompany.hospital.domain.HistoriaClinicaTestSamples.*;
import static com.mycompany.hospital.domain.MedicoTestSamples.*;
import static com.mycompany.hospital.domain.PacienteTestSamples.*;
import static com.mycompany.hospital.domain.TurnoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsultaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consulta.class);
        Consulta consulta1 = getConsultaSample1();
        Consulta consulta2 = new Consulta();
        assertThat(consulta1).isNotEqualTo(consulta2);

        consulta2.setId(consulta1.getId());
        assertThat(consulta1).isEqualTo(consulta2);

        consulta2 = getConsultaSample2();
        assertThat(consulta1).isNotEqualTo(consulta2);
    }

    @Test
    void turnoTest() {
        Consulta consulta = getConsultaRandomSampleGenerator();
        Turno turnoBack = getTurnoRandomSampleGenerator();

        consulta.setTurno(turnoBack);
        assertThat(consulta.getTurno()).isEqualTo(turnoBack);

        consulta.turno(null);
        assertThat(consulta.getTurno()).isNull();
    }

    @Test
    void pacienteTest() {
        Consulta consulta = getConsultaRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        consulta.setPaciente(pacienteBack);
        assertThat(consulta.getPaciente()).isEqualTo(pacienteBack);

        consulta.paciente(null);
        assertThat(consulta.getPaciente()).isNull();
    }

    @Test
    void medicoTest() {
        Consulta consulta = getConsultaRandomSampleGenerator();
        Medico medicoBack = getMedicoRandomSampleGenerator();

        consulta.setMedico(medicoBack);
        assertThat(consulta.getMedico()).isEqualTo(medicoBack);

        consulta.medico(null);
        assertThat(consulta.getMedico()).isNull();
    }

    @Test
    void historiaClinicaTest() {
        Consulta consulta = getConsultaRandomSampleGenerator();
        HistoriaClinica historiaClinicaBack = getHistoriaClinicaRandomSampleGenerator();

        consulta.setHistoriaClinica(historiaClinicaBack);
        assertThat(consulta.getHistoriaClinica()).isEqualTo(historiaClinicaBack);

        consulta.historiaClinica(null);
        assertThat(consulta.getHistoriaClinica()).isNull();
    }
}
