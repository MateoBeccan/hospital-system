package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.EspecialidadTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EspecialidadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Especialidad.class);
        Especialidad especialidad1 = getEspecialidadSample1();
        Especialidad especialidad2 = new Especialidad();
        assertThat(especialidad1).isNotEqualTo(especialidad2);

        especialidad2.setId(especialidad1.getId());
        assertThat(especialidad1).isEqualTo(especialidad2);

        especialidad2 = getEspecialidadSample2();
        assertThat(especialidad1).isNotEqualTo(especialidad2);
    }
}
