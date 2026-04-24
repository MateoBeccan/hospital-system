package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.EstadoLaboralTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstadoLaboralTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstadoLaboral.class);
        EstadoLaboral estadoLaboral1 = getEstadoLaboralSample1();
        EstadoLaboral estadoLaboral2 = new EstadoLaboral();
        assertThat(estadoLaboral1).isNotEqualTo(estadoLaboral2);

        estadoLaboral2.setId(estadoLaboral1.getId());
        assertThat(estadoLaboral1).isEqualTo(estadoLaboral2);

        estadoLaboral2 = getEstadoLaboralSample2();
        assertThat(estadoLaboral1).isNotEqualTo(estadoLaboral2);
    }
}
