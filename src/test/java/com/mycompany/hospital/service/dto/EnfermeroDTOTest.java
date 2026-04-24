package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnfermeroDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnfermeroDTO.class);
        EnfermeroDTO enfermeroDTO1 = new EnfermeroDTO();
        enfermeroDTO1.setId(1L);
        EnfermeroDTO enfermeroDTO2 = new EnfermeroDTO();
        assertThat(enfermeroDTO1).isNotEqualTo(enfermeroDTO2);
        enfermeroDTO2.setId(enfermeroDTO1.getId());
        assertThat(enfermeroDTO1).isEqualTo(enfermeroDTO2);
        enfermeroDTO2.setId(2L);
        assertThat(enfermeroDTO1).isNotEqualTo(enfermeroDTO2);
        enfermeroDTO1.setId(null);
        assertThat(enfermeroDTO1).isNotEqualTo(enfermeroDTO2);
    }
}
