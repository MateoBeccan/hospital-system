package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AntecedenteClinicoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AntecedenteClinicoDTO.class);
        AntecedenteClinicoDTO antecedenteClinicoDTO1 = new AntecedenteClinicoDTO();
        antecedenteClinicoDTO1.setId(1L);
        AntecedenteClinicoDTO antecedenteClinicoDTO2 = new AntecedenteClinicoDTO();
        assertThat(antecedenteClinicoDTO1).isNotEqualTo(antecedenteClinicoDTO2);
        antecedenteClinicoDTO2.setId(antecedenteClinicoDTO1.getId());
        assertThat(antecedenteClinicoDTO1).isEqualTo(antecedenteClinicoDTO2);
        antecedenteClinicoDTO2.setId(2L);
        assertThat(antecedenteClinicoDTO1).isNotEqualTo(antecedenteClinicoDTO2);
        antecedenteClinicoDTO1.setId(null);
        assertThat(antecedenteClinicoDTO1).isNotEqualTo(antecedenteClinicoDTO2);
    }
}
