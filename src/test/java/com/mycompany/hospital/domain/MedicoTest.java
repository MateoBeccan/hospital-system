package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.EmpleadoTestSamples.*;
import static com.mycompany.hospital.domain.EspecialidadTestSamples.*;
import static com.mycompany.hospital.domain.MedicoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medico.class);
        Medico medico1 = getMedicoSample1();
        Medico medico2 = new Medico();
        assertThat(medico1).isNotEqualTo(medico2);

        medico2.setId(medico1.getId());
        assertThat(medico1).isEqualTo(medico2);

        medico2 = getMedicoSample2();
        assertThat(medico1).isNotEqualTo(medico2);
    }

    @Test
    void empleadoTest() {
        Medico medico = getMedicoRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        medico.setEmpleado(empleadoBack);
        assertThat(medico.getEmpleado()).isEqualTo(empleadoBack);

        medico.empleado(null);
        assertThat(medico.getEmpleado()).isNull();
    }

    @Test
    void especialidadTest() {
        Medico medico = getMedicoRandomSampleGenerator();
        Especialidad especialidadBack = getEspecialidadRandomSampleGenerator();

        medico.setEspecialidad(especialidadBack);
        assertThat(medico.getEspecialidad()).isEqualTo(especialidadBack);

        medico.especialidad(null);
        assertThat(medico.getEspecialidad()).isNull();
    }
}
