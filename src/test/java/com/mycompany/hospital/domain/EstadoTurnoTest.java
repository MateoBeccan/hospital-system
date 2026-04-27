package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.EstadoTurnoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EstadoTurnoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstadoTurno.class);
        EstadoTurno estadoTurno1 = getEstadoTurnoSample1();
        EstadoTurno estadoTurno2 = new EstadoTurno();
        assertThat(estadoTurno1).isNotEqualTo(estadoTurno2);

        estadoTurno2.setId(estadoTurno1.getId());
        assertThat(estadoTurno1).isEqualTo(estadoTurno2);

        estadoTurno2 = getEstadoTurnoSample2();
        assertThat(estadoTurno1).isNotEqualTo(estadoTurno2);
    }
}
