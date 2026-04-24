package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.CiudadTestSamples.*;
import static com.mycompany.hospital.domain.EmpleadoTestSamples.*;
import static com.mycompany.hospital.domain.PacienteTestSamples.*;
import static com.mycompany.hospital.domain.PersonaTestSamples.*;
import static com.mycompany.hospital.domain.SexoTestSamples.*;
import static com.mycompany.hospital.domain.TipoDocumentoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Persona.class);
        Persona persona1 = getPersonaSample1();
        Persona persona2 = new Persona();
        assertThat(persona1).isNotEqualTo(persona2);

        persona2.setId(persona1.getId());
        assertThat(persona1).isEqualTo(persona2);

        persona2 = getPersonaSample2();
        assertThat(persona1).isNotEqualTo(persona2);
    }

    @Test
    void tipoDocumentoTest() {
        Persona persona = getPersonaRandomSampleGenerator();
        TipoDocumento tipoDocumentoBack = getTipoDocumentoRandomSampleGenerator();

        persona.setTipoDocumento(tipoDocumentoBack);
        assertThat(persona.getTipoDocumento()).isEqualTo(tipoDocumentoBack);

        persona.tipoDocumento(null);
        assertThat(persona.getTipoDocumento()).isNull();
    }

    @Test
    void sexoTest() {
        Persona persona = getPersonaRandomSampleGenerator();
        Sexo sexoBack = getSexoRandomSampleGenerator();

        persona.setSexo(sexoBack);
        assertThat(persona.getSexo()).isEqualTo(sexoBack);

        persona.sexo(null);
        assertThat(persona.getSexo()).isNull();
    }

    @Test
    void ciudadTest() {
        Persona persona = getPersonaRandomSampleGenerator();
        Ciudad ciudadBack = getCiudadRandomSampleGenerator();

        persona.setCiudad(ciudadBack);
        assertThat(persona.getCiudad()).isEqualTo(ciudadBack);

        persona.ciudad(null);
        assertThat(persona.getCiudad()).isNull();
    }

    @Test
    void pacienteTest() {
        Persona persona = getPersonaRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        persona.setPaciente(pacienteBack);
        assertThat(persona.getPaciente()).isEqualTo(pacienteBack);
        assertThat(pacienteBack.getPersona()).isEqualTo(persona);

        persona.paciente(null);
        assertThat(persona.getPaciente()).isNull();
        assertThat(pacienteBack.getPersona()).isNull();
    }

    @Test
    void empleadoTest() {
        Persona persona = getPersonaRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        persona.setEmpleado(empleadoBack);
        assertThat(persona.getEmpleado()).isEqualTo(empleadoBack);
        assertThat(empleadoBack.getPersona()).isEqualTo(persona);

        persona.empleado(null);
        assertThat(persona.getEmpleado()).isNull();
        assertThat(empleadoBack.getPersona()).isNull();
    }
}
