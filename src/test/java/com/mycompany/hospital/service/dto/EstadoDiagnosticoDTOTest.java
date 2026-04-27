package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstadoDiagnosticoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstadoDiagnosticoDTO.class);
        EstadoDiagnosticoDTO estadoDiagnosticoDTO1 = new EstadoDiagnosticoDTO();
        estadoDiagnosticoDTO1.setId(1L);
        EstadoDiagnosticoDTO estadoDiagnosticoDTO2 = new EstadoDiagnosticoDTO();
        assertThat(estadoDiagnosticoDTO1).isNotEqualTo(estadoDiagnosticoDTO2);
        estadoDiagnosticoDTO2.setId(estadoDiagnosticoDTO1.getId());
        assertThat(estadoDiagnosticoDTO1).isEqualTo(estadoDiagnosticoDTO2);
        estadoDiagnosticoDTO2.setId(2L);
        assertThat(estadoDiagnosticoDTO1).isNotEqualTo(estadoDiagnosticoDTO2);
        estadoDiagnosticoDTO1.setId(null);
        assertThat(estadoDiagnosticoDTO1).isNotEqualTo(estadoDiagnosticoDTO2);
    }
}
