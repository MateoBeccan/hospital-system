package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.EstadoTratamientoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstadoTratamientoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstadoTratamiento.class);
        EstadoTratamiento estadoTratamiento1 = getEstadoTratamientoSample1();
        EstadoTratamiento estadoTratamiento2 = new EstadoTratamiento();
        assertThat(estadoTratamiento1).isNotEqualTo(estadoTratamiento2);

        estadoTratamiento2.setId(estadoTratamiento1.getId());
        assertThat(estadoTratamiento1).isEqualTo(estadoTratamiento2);

        estadoTratamiento2 = getEstadoTratamientoSample2();
        assertThat(estadoTratamiento1).isNotEqualTo(estadoTratamiento2);
    }
}
