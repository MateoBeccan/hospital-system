package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.TipoDiagnosticoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoDiagnosticoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoDiagnostico.class);
        TipoDiagnostico tipoDiagnostico1 = getTipoDiagnosticoSample1();
        TipoDiagnostico tipoDiagnostico2 = new TipoDiagnostico();
        assertThat(tipoDiagnostico1).isNotEqualTo(tipoDiagnostico2);

        tipoDiagnostico2.setId(tipoDiagnostico1.getId());
        assertThat(tipoDiagnostico1).isEqualTo(tipoDiagnostico2);

        tipoDiagnostico2 = getTipoDiagnosticoSample2();
        assertThat(tipoDiagnostico1).isNotEqualTo(tipoDiagnostico2);
    }
}
