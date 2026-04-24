package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.ObraSocialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObraSocialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ObraSocial.class);
        ObraSocial obraSocial1 = getObraSocialSample1();
        ObraSocial obraSocial2 = new ObraSocial();
        assertThat(obraSocial1).isNotEqualTo(obraSocial2);

        obraSocial2.setId(obraSocial1.getId());
        assertThat(obraSocial1).isEqualTo(obraSocial2);

        obraSocial2 = getObraSocialSample2();
        assertThat(obraSocial1).isNotEqualTo(obraSocial2);
    }
}
