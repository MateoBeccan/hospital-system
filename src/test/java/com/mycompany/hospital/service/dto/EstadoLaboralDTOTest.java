package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstadoLaboralDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstadoLaboralDTO.class);
        EstadoLaboralDTO estadoLaboralDTO1 = new EstadoLaboralDTO();
        estadoLaboralDTO1.setId(1L);
        EstadoLaboralDTO estadoLaboralDTO2 = new EstadoLaboralDTO();
        assertThat(estadoLaboralDTO1).isNotEqualTo(estadoLaboralDTO2);
        estadoLaboralDTO2.setId(estadoLaboralDTO1.getId());
        assertThat(estadoLaboralDTO1).isEqualTo(estadoLaboralDTO2);
        estadoLaboralDTO2.setId(2L);
        assertThat(estadoLaboralDTO1).isNotEqualTo(estadoLaboralDTO2);
        estadoLaboralDTO1.setId(null);
        assertThat(estadoLaboralDTO1).isNotEqualTo(estadoLaboralDTO2);
    }
}
