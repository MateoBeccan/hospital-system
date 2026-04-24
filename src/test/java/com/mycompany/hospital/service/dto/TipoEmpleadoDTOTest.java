package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoEmpleadoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoEmpleadoDTO.class);
        TipoEmpleadoDTO tipoEmpleadoDTO1 = new TipoEmpleadoDTO();
        tipoEmpleadoDTO1.setId(1L);
        TipoEmpleadoDTO tipoEmpleadoDTO2 = new TipoEmpleadoDTO();
        assertThat(tipoEmpleadoDTO1).isNotEqualTo(tipoEmpleadoDTO2);
        tipoEmpleadoDTO2.setId(tipoEmpleadoDTO1.getId());
        assertThat(tipoEmpleadoDTO1).isEqualTo(tipoEmpleadoDTO2);
        tipoEmpleadoDTO2.setId(2L);
        assertThat(tipoEmpleadoDTO1).isNotEqualTo(tipoEmpleadoDTO2);
        tipoEmpleadoDTO1.setId(null);
        assertThat(tipoEmpleadoDTO1).isNotEqualTo(tipoEmpleadoDTO2);
    }
}
