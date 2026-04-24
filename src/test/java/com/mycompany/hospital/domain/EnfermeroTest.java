package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.EmpleadoTestSamples.*;
import static com.mycompany.hospital.domain.EnfermeroTestSamples.*;
import static com.mycompany.hospital.domain.TurnoLaboralTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnfermeroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enfermero.class);
        Enfermero enfermero1 = getEnfermeroSample1();
        Enfermero enfermero2 = new Enfermero();
        assertThat(enfermero1).isNotEqualTo(enfermero2);

        enfermero2.setId(enfermero1.getId());
        assertThat(enfermero1).isEqualTo(enfermero2);

        enfermero2 = getEnfermeroSample2();
        assertThat(enfermero1).isNotEqualTo(enfermero2);
    }

    @Test
    void empleadoTest() {
        Enfermero enfermero = getEnfermeroRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        enfermero.setEmpleado(empleadoBack);
        assertThat(enfermero.getEmpleado()).isEqualTo(empleadoBack);

        enfermero.empleado(null);
        assertThat(enfermero.getEmpleado()).isNull();
    }

    @Test
    void turnoLaboralTest() {
        Enfermero enfermero = getEnfermeroRandomSampleGenerator();
        TurnoLaboral turnoLaboralBack = getTurnoLaboralRandomSampleGenerator();

        enfermero.setTurnoLaboral(turnoLaboralBack);
        assertThat(enfermero.getTurnoLaboral()).isEqualTo(turnoLaboralBack);

        enfermero.turnoLaboral(null);
        assertThat(enfermero.getTurnoLaboral()).isNull();
    }
}
