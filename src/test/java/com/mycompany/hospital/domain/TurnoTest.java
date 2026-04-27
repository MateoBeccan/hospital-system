package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.CanalSolicitudTestSamples.*;
import static com.mycompany.hospital.domain.ConsultaTestSamples.*;
import static com.mycompany.hospital.domain.EspecialidadTestSamples.*;
import static com.mycompany.hospital.domain.EstadoTurnoTestSamples.*;
import static com.mycompany.hospital.domain.MedicoTestSamples.*;
import static com.mycompany.hospital.domain.PacienteTestSamples.*;
import static com.mycompany.hospital.domain.TurnoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TurnoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Turno.class);
        Turno turno1 = getTurnoSample1();
        Turno turno2 = new Turno();
        assertThat(turno1).isNotEqualTo(turno2);

        turno2.setId(turno1.getId());
        assertThat(turno1).isEqualTo(turno2);

        turno2 = getTurnoSample2();
        assertThat(turno1).isNotEqualTo(turno2);
    }

    @Test
    void pacienteTest() {
        Turno turno = getTurnoRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        turno.setPaciente(pacienteBack);
        assertThat(turno.getPaciente()).isEqualTo(pacienteBack);

        turno.paciente(null);
        assertThat(turno.getPaciente()).isNull();
    }

    @Test
    void medicoTest() {
        Turno turno = getTurnoRandomSampleGenerator();
        Medico medicoBack = getMedicoRandomSampleGenerator();

        turno.setMedico(medicoBack);
        assertThat(turno.getMedico()).isEqualTo(medicoBack);

        turno.medico(null);
        assertThat(turno.getMedico()).isNull();
    }

    @Test
    void especialidadTest() {
        Turno turno = getTurnoRandomSampleGenerator();
        Especialidad especialidadBack = getEspecialidadRandomSampleGenerator();

        turno.setEspecialidad(especialidadBack);
        assertThat(turno.getEspecialidad()).isEqualTo(especialidadBack);

        turno.especialidad(null);
        assertThat(turno.getEspecialidad()).isNull();
    }

    @Test
    void estadoTurnoTest() {
        Turno turno = getTurnoRandomSampleGenerator();
        EstadoTurno estadoTurnoBack = getEstadoTurnoRandomSampleGenerator();

        turno.setEstadoTurno(estadoTurnoBack);
        assertThat(turno.getEstadoTurno()).isEqualTo(estadoTurnoBack);

        turno.estadoTurno(null);
        assertThat(turno.getEstadoTurno()).isNull();
    }

    @Test
    void canalSolicitudTest() {
        Turno turno = getTurnoRandomSampleGenerator();
        CanalSolicitud canalSolicitudBack = getCanalSolicitudRandomSampleGenerator();

        turno.setCanalSolicitud(canalSolicitudBack);
        assertThat(turno.getCanalSolicitud()).isEqualTo(canalSolicitudBack);

        turno.canalSolicitud(null);
        assertThat(turno.getCanalSolicitud()).isNull();
    }

    @Test
    void consultaTest() {
        Turno turno = getTurnoRandomSampleGenerator();
        Consulta consultaBack = getConsultaRandomSampleGenerator();

        turno.setConsulta(consultaBack);
        assertThat(turno.getConsulta()).isEqualTo(consultaBack);
        assertThat(consultaBack.getTurno()).isEqualTo(turno);

        turno.consulta(null);
        assertThat(turno.getConsulta()).isNull();
        assertThat(consultaBack.getTurno()).isNull();
    }
}
