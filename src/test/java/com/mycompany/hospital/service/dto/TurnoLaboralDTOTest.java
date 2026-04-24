package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TurnoLaboralDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TurnoLaboralDTO.class);
        TurnoLaboralDTO turnoLaboralDTO1 = new TurnoLaboralDTO();
        turnoLaboralDTO1.setId(1L);
        TurnoLaboralDTO turnoLaboralDTO2 = new TurnoLaboralDTO();
        assertThat(turnoLaboralDTO1).isNotEqualTo(turnoLaboralDTO2);
        turnoLaboralDTO2.setId(turnoLaboralDTO1.getId());
        assertThat(turnoLaboralDTO1).isEqualTo(turnoLaboralDTO2);
        turnoLaboralDTO2.setId(2L);
        assertThat(turnoLaboralDTO1).isNotEqualTo(turnoLaboralDTO2);
        turnoLaboralDTO1.setId(null);
        assertThat(turnoLaboralDTO1).isNotEqualTo(turnoLaboralDTO2);
    }
}
