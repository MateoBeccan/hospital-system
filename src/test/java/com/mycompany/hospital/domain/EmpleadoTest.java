package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.CargoTestSamples.*;
import static com.mycompany.hospital.domain.EmpleadoTestSamples.*;
import static com.mycompany.hospital.domain.EnfermeroTestSamples.*;
import static com.mycompany.hospital.domain.EstadoLaboralTestSamples.*;
import static com.mycompany.hospital.domain.MedicoTestSamples.*;
import static com.mycompany.hospital.domain.PersonaTestSamples.*;
import static com.mycompany.hospital.domain.TipoEmpleadoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmpleadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Empleado.class);
        Empleado empleado1 = getEmpleadoSample1();
        Empleado empleado2 = new Empleado();
        assertThat(empleado1).isNotEqualTo(empleado2);

        empleado2.setId(empleado1.getId());
        assertThat(empleado1).isEqualTo(empleado2);

        empleado2 = getEmpleadoSample2();
        assertThat(empleado1).isNotEqualTo(empleado2);
    }

    @Test
    void personaTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Persona personaBack = getPersonaRandomSampleGenerator();

        empleado.setPersona(personaBack);
        assertThat(empleado.getPersona()).isEqualTo(personaBack);

        empleado.persona(null);
        assertThat(empleado.getPersona()).isNull();
    }

    @Test
    void tipoEmpleadoTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        TipoEmpleado tipoEmpleadoBack = getTipoEmpleadoRandomSampleGenerator();

        empleado.setTipoEmpleado(tipoEmpleadoBack);
        assertThat(empleado.getTipoEmpleado()).isEqualTo(tipoEmpleadoBack);

        empleado.tipoEmpleado(null);
        assertThat(empleado.getTipoEmpleado()).isNull();
    }

    @Test
    void estadoLaboralTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        EstadoLaboral estadoLaboralBack = getEstadoLaboralRandomSampleGenerator();

        empleado.setEstadoLaboral(estadoLaboralBack);
        assertThat(empleado.getEstadoLaboral()).isEqualTo(estadoLaboralBack);

        empleado.estadoLaboral(null);
        assertThat(empleado.getEstadoLaboral()).isNull();
    }

    @Test
    void cargoTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Cargo cargoBack = getCargoRandomSampleGenerator();

        empleado.setCargo(cargoBack);
        assertThat(empleado.getCargo()).isEqualTo(cargoBack);

        empleado.cargo(null);
        assertThat(empleado.getCargo()).isNull();
    }

    @Test
    void medicoTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Medico medicoBack = getMedicoRandomSampleGenerator();

        empleado.setMedico(medicoBack);
        assertThat(empleado.getMedico()).isEqualTo(medicoBack);
        assertThat(medicoBack.getEmpleado()).isEqualTo(empleado);

        empleado.medico(null);
        assertThat(empleado.getMedico()).isNull();
        assertThat(medicoBack.getEmpleado()).isNull();
    }

    @Test
    void enfermeroTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Enfermero enfermeroBack = getEnfermeroRandomSampleGenerator();

        empleado.setEnfermero(enfermeroBack);
        assertThat(empleado.getEnfermero()).isEqualTo(enfermeroBack);
        assertThat(enfermeroBack.getEmpleado()).isEqualTo(empleado);

        empleado.enfermero(null);
        assertThat(empleado.getEnfermero()).isNull();
        assertThat(enfermeroBack.getEmpleado()).isNull();
    }
}
