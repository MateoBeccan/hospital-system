package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstadoTratamientoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstadoTratamientoDTO.class);
        EstadoTratamientoDTO estadoTratamientoDTO1 = new EstadoTratamientoDTO();
        estadoTratamientoDTO1.setId(1L);
        EstadoTratamientoDTO estadoTratamientoDTO2 = new EstadoTratamientoDTO();
        assertThat(estadoTratamientoDTO1).isNotEqualTo(estadoTratamientoDTO2);
        estadoTratamientoDTO2.setId(estadoTratamientoDTO1.getId());
        assertThat(estadoTratamientoDTO1).isEqualTo(estadoTratamientoDTO2);
        estadoTratamientoDTO2.setId(2L);
        assertThat(estadoTratamientoDTO1).isNotEqualTo(estadoTratamientoDTO2);
        estadoTratamientoDTO1.setId(null);
        assertThat(estadoTratamientoDTO1).isNotEqualTo(estadoTratamientoDTO2);
    }
}
