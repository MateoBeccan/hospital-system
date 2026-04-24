package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactorRhDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactorRhDTO.class);
        FactorRhDTO factorRhDTO1 = new FactorRhDTO();
        factorRhDTO1.setId(1L);
        FactorRhDTO factorRhDTO2 = new FactorRhDTO();
        assertThat(factorRhDTO1).isNotEqualTo(factorRhDTO2);
        factorRhDTO2.setId(factorRhDTO1.getId());
        assertThat(factorRhDTO1).isEqualTo(factorRhDTO2);
        factorRhDTO2.setId(2L);
        assertThat(factorRhDTO1).isNotEqualTo(factorRhDTO2);
        factorRhDTO1.setId(null);
        assertThat(factorRhDTO1).isNotEqualTo(factorRhDTO2);
    }
}
