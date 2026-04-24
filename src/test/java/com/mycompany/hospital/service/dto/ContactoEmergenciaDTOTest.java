package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactoEmergenciaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactoEmergenciaDTO.class);
        ContactoEmergenciaDTO contactoEmergenciaDTO1 = new ContactoEmergenciaDTO();
        contactoEmergenciaDTO1.setId(1L);
        ContactoEmergenciaDTO contactoEmergenciaDTO2 = new ContactoEmergenciaDTO();
        assertThat(contactoEmergenciaDTO1).isNotEqualTo(contactoEmergenciaDTO2);
        contactoEmergenciaDTO2.setId(contactoEmergenciaDTO1.getId());
        assertThat(contactoEmergenciaDTO1).isEqualTo(contactoEmergenciaDTO2);
        contactoEmergenciaDTO2.setId(2L);
        assertThat(contactoEmergenciaDTO1).isNotEqualTo(contactoEmergenciaDTO2);
        contactoEmergenciaDTO1.setId(null);
        assertThat(contactoEmergenciaDTO1).isNotEqualTo(contactoEmergenciaDTO2);
    }
}
