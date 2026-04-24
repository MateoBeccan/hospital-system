package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObraSocialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ObraSocialDTO.class);
        ObraSocialDTO obraSocialDTO1 = new ObraSocialDTO();
        obraSocialDTO1.setId(1L);
        ObraSocialDTO obraSocialDTO2 = new ObraSocialDTO();
        assertThat(obraSocialDTO1).isNotEqualTo(obraSocialDTO2);
        obraSocialDTO2.setId(obraSocialDTO1.getId());
        assertThat(obraSocialDTO1).isEqualTo(obraSocialDTO2);
        obraSocialDTO2.setId(2L);
        assertThat(obraSocialDTO1).isNotEqualTo(obraSocialDTO2);
        obraSocialDTO1.setId(null);
        assertThat(obraSocialDTO1).isNotEqualTo(obraSocialDTO2);
    }
}
