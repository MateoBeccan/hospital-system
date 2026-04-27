package com.mycompany.hospital.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoDiagnosticoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoDiagnosticoDTO.class);
        TipoDiagnosticoDTO tipoDiagnosticoDTO1 = new TipoDiagnosticoDTO();
        tipoDiagnosticoDTO1.setId(1L);
        TipoDiagnosticoDTO tipoDiagnosticoDTO2 = new TipoDiagnosticoDTO();
        assertThat(tipoDiagnosticoDTO1).isNotEqualTo(tipoDiagnosticoDTO2);
        tipoDiagnosticoDTO2.setId(tipoDiagnosticoDTO1.getId());
        assertThat(tipoDiagnosticoDTO1).isEqualTo(tipoDiagnosticoDTO2);
        tipoDiagnosticoDTO2.setId(2L);
        assertThat(tipoDiagnosticoDTO1).isNotEqualTo(tipoDiagnosticoDTO2);
        tipoDiagnosticoDTO1.setId(null);
        assertThat(tipoDiagnosticoDTO1).isNotEqualTo(tipoDiagnosticoDTO2);
    }
}
