package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.TurnoLaboralTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TurnoLaboralTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TurnoLaboral.class);
        TurnoLaboral turnoLaboral1 = getTurnoLaboralSample1();
        TurnoLaboral turnoLaboral2 = new TurnoLaboral();
        assertThat(turnoLaboral1).isNotEqualTo(turnoLaboral2);

        turnoLaboral2.setId(turnoLaboral1.getId());
        assertThat(turnoLaboral1).isEqualTo(turnoLaboral2);

        turnoLaboral2 = getTurnoLaboralSample2();
        assertThat(turnoLaboral1).isNotEqualTo(turnoLaboral2);
    }
}
