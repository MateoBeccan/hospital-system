package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SexoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SexoDTO.class);
        SexoDTO sexoDTO1 = new SexoDTO();
        sexoDTO1.setId(1L);
        SexoDTO sexoDTO2 = new SexoDTO();
        assertThat(sexoDTO1).isNotEqualTo(sexoDTO2);
        sexoDTO2.setId(sexoDTO1.getId());
        assertThat(sexoDTO1).isEqualTo(sexoDTO2);
        sexoDTO2.setId(2L);
        assertThat(sexoDTO1).isNotEqualTo(sexoDTO2);
        sexoDTO1.setId(null);
        assertThat(sexoDTO1).isNotEqualTo(sexoDTO2);
    }
}
