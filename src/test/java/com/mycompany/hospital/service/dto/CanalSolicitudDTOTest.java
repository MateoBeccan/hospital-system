package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CanalSolicitudDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CanalSolicitudDTO.class);
        CanalSolicitudDTO canalSolicitudDTO1 = new CanalSolicitudDTO();
        canalSolicitudDTO1.setId(1L);
        CanalSolicitudDTO canalSolicitudDTO2 = new CanalSolicitudDTO();
        assertThat(canalSolicitudDTO1).isNotEqualTo(canalSolicitudDTO2);
        canalSolicitudDTO2.setId(canalSolicitudDTO1.getId());
        assertThat(canalSolicitudDTO1).isEqualTo(canalSolicitudDTO2);
        canalSolicitudDTO2.setId(2L);
        assertThat(canalSolicitudDTO1).isNotEqualTo(canalSolicitudDTO2);
        canalSolicitudDTO1.setId(null);
        assertThat(canalSolicitudDTO1).isNotEqualTo(canalSolicitudDTO2);
    }
}
