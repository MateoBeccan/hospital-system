package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.EstadoDiagnosticoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstadoDiagnosticoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstadoDiagnostico.class);
        EstadoDiagnostico estadoDiagnostico1 = getEstadoDiagnosticoSample1();
        EstadoDiagnostico estadoDiagnostico2 = new EstadoDiagnostico();
        assertThat(estadoDiagnostico1).isNotEqualTo(estadoDiagnostico2);

        estadoDiagnostico2.setId(estadoDiagnostico1.getId());
        assertThat(estadoDiagnostico1).isEqualTo(estadoDiagnostico2);

        estadoDiagnostico2 = getEstadoDiagnosticoSample2();
        assertThat(estadoDiagnostico1).isNotEqualTo(estadoDiagnostico2);
    }
}
