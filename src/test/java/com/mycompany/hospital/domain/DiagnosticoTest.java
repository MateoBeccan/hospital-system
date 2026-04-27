package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.ConsultaTestSamples.*;
import static com.mycompany.hospital.domain.DiagnosticoTestSamples.*;
import static com.mycompany.hospital.domain.EstadoDiagnosticoTestSamples.*;
import static com.mycompany.hospital.domain.MedicoTestSamples.*;
import static com.mycompany.hospital.domain.PacienteTestSamples.*;
import static com.mycompany.hospital.domain.TipoDiagnosticoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DiagnosticoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Diagnostico.class);
        Diagnostico diagnostico1 = getDiagnosticoSample1();
        Diagnostico diagnostico2 = new Diagnostico();
        assertThat(diagnostico1).isNotEqualTo(diagnostico2);

        diagnostico2.setId(diagnostico1.getId());
        assertThat(diagnostico1).isEqualTo(diagnostico2);

        diagnostico2 = getDiagnosticoSample2();
        assertThat(diagnostico1).isNotEqualTo(diagnostico2);
    }

    @Test
    void consultaTest() {
        Diagnostico diagnostico = getDiagnosticoRandomSampleGenerator();
        Consulta consultaBack = getConsultaRandomSampleGenerator();

        diagnostico.setConsulta(consultaBack);
        assertThat(diagnostico.getConsulta()).isEqualTo(consultaBack);

        diagnostico.consulta(null);
        assertThat(diagnostico.getConsulta()).isNull();
    }

    @Test
    void pacienteTest() {
        Diagnostico diagnostico = getDiagnosticoRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        diagnostico.setPaciente(pacienteBack);
        assertThat(diagnostico.getPaciente()).isEqualTo(pacienteBack);

        diagnostico.paciente(null);
        assertThat(diagnostico.getPaciente()).isNull();
    }

    @Test
    void medicoTest() {
        Diagnostico diagnostico = getDiagnosticoRandomSampleGenerator();
        Medico medicoBack = getMedicoRandomSampleGenerator();

        diagnostico.setMedico(medicoBack);
        assertThat(diagnostico.getMedico()).isEqualTo(medicoBack);

        diagnostico.medico(null);
        assertThat(diagnostico.getMedico()).isNull();
    }

    @Test
    void tipoDiagnosticoTest() {
        Diagnostico diagnostico = getDiagnosticoRandomSampleGenerator();
        TipoDiagnostico tipoDiagnosticoBack = getTipoDiagnosticoRandomSampleGenerator();

        diagnostico.setTipoDiagnostico(tipoDiagnosticoBack);
        assertThat(diagnostico.getTipoDiagnostico()).isEqualTo(tipoDiagnosticoBack);

        diagnostico.tipoDiagnostico(null);
        assertThat(diagnostico.getTipoDiagnostico()).isNull();
    }

    @Test
    void estadoDiagnosticoTest() {
        Diagnostico diagnostico = getDiagnosticoRandomSampleGenerator();
        EstadoDiagnostico estadoDiagnosticoBack = getEstadoDiagnosticoRandomSampleGenerator();

        diagnostico.setEstadoDiagnostico(estadoDiagnosticoBack);
        assertThat(diagnostico.getEstadoDiagnostico()).isEqualTo(estadoDiagnosticoBack);

        diagnostico.estadoDiagnostico(null);
        assertThat(diagnostico.getEstadoDiagnostico()).isNull();
    }
}
