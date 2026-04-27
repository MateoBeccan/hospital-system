package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.CanalSolicitudTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CanalSolicitudTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CanalSolicitud.class);
        CanalSolicitud canalSolicitud1 = getCanalSolicitudSample1();
        CanalSolicitud canalSolicitud2 = new CanalSolicitud();
        assertThat(canalSolicitud1).isNotEqualTo(canalSolicitud2);

        canalSolicitud2.setId(canalSolicitud1.getId());
        assertThat(canalSolicitud1).isEqualTo(canalSolicitud2);

        canalSolicitud2 = getCanalSolicitudSample2();
        assertThat(canalSolicitud1).isNotEqualTo(canalSolicitud2);
    }
}
