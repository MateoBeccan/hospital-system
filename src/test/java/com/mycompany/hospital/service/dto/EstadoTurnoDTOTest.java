package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstadoTurnoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstadoTurnoDTO.class);
        EstadoTurnoDTO estadoTurnoDTO1 = new EstadoTurnoDTO();
        estadoTurnoDTO1.setId(1L);
        EstadoTurnoDTO estadoTurnoDTO2 = new EstadoTurnoDTO();
        assertThat(estadoTurnoDTO1).isNotEqualTo(estadoTurnoDTO2);
        estadoTurnoDTO2.setId(estadoTurnoDTO1.getId());
        assertThat(estadoTurnoDTO1).isEqualTo(estadoTurnoDTO2);
        estadoTurnoDTO2.setId(2L);
        assertThat(estadoTurnoDTO1).isNotEqualTo(estadoTurnoDTO2);
        estadoTurnoDTO1.setId(null);
        assertThat(estadoTurnoDTO1).isNotEqualTo(estadoTurnoDTO2);
    }
}
