package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GrupoSanguineoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GrupoSanguineoDTO.class);
        GrupoSanguineoDTO grupoSanguineoDTO1 = new GrupoSanguineoDTO();
        grupoSanguineoDTO1.setId(1L);
        GrupoSanguineoDTO grupoSanguineoDTO2 = new GrupoSanguineoDTO();
        assertThat(grupoSanguineoDTO1).isNotEqualTo(grupoSanguineoDTO2);
        grupoSanguineoDTO2.setId(grupoSanguineoDTO1.getId());
        assertThat(grupoSanguineoDTO1).isEqualTo(grupoSanguineoDTO2);
        grupoSanguineoDTO2.setId(2L);
        assertThat(grupoSanguineoDTO1).isNotEqualTo(grupoSanguineoDTO2);
        grupoSanguineoDTO1.setId(null);
        assertThat(grupoSanguineoDTO1).isNotEqualTo(grupoSanguineoDTO2);
    }
}
