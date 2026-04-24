package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.ContactoEmergenciaTestSamples.*;
import static com.mycompany.hospital.domain.PersonaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactoEmergenciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactoEmergencia.class);
        ContactoEmergencia contactoEmergencia1 = getContactoEmergenciaSample1();
        ContactoEmergencia contactoEmergencia2 = new ContactoEmergencia();
        assertThat(contactoEmergencia1).isNotEqualTo(contactoEmergencia2);

        contactoEmergencia2.setId(contactoEmergencia1.getId());
        assertThat(contactoEmergencia1).isEqualTo(contactoEmergencia2);

        contactoEmergencia2 = getContactoEmergenciaSample2();
        assertThat(contactoEmergencia1).isNotEqualTo(contactoEmergencia2);
    }

    @Test
    void personaTest() {
        ContactoEmergencia contactoEmergencia = getContactoEmergenciaRandomSampleGenerator();
        Persona personaBack = getPersonaRandomSampleGenerator();

        contactoEmergencia.setPersona(personaBack);
        assertThat(contactoEmergencia.getPersona()).isEqualTo(personaBack);

        contactoEmergencia.persona(null);
        assertThat(contactoEmergencia.getPersona()).isNull();
    }
}
