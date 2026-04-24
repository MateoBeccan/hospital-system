package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.TipoEmpleadoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TipoEmpleadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoEmpleado.class);
        TipoEmpleado tipoEmpleado1 = getTipoEmpleadoSample1();
        TipoEmpleado tipoEmpleado2 = new TipoEmpleado();
        assertThat(tipoEmpleado1).isNotEqualTo(tipoEmpleado2);

        tipoEmpleado2.setId(tipoEmpleado1.getId());
        assertThat(tipoEmpleado1).isEqualTo(tipoEmpleado2);

        tipoEmpleado2 = getTipoEmpleadoSample2();
        assertThat(tipoEmpleado1).isNotEqualTo(tipoEmpleado2);
    }
}
