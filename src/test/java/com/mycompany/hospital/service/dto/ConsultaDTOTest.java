package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsultaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsultaDTO.class);
        ConsultaDTO consultaDTO1 = new ConsultaDTO();
        consultaDTO1.setId(1L);
        ConsultaDTO consultaDTO2 = new ConsultaDTO();
        assertThat(consultaDTO1).isNotEqualTo(consultaDTO2);
        consultaDTO2.setId(consultaDTO1.getId());
        assertThat(consultaDTO1).isEqualTo(consultaDTO2);
        consultaDTO2.setId(2L);
        assertThat(consultaDTO1).isNotEqualTo(consultaDTO2);
        consultaDTO1.setId(null);
        assertThat(consultaDTO1).isNotEqualTo(consultaDTO2);
    }
}
