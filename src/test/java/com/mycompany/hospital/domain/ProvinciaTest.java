package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.PaisTestSamples.*;
import static com.mycompany.hospital.domain.ProvinciaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProvinciaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Provincia.class);
        Provincia provincia1 = getProvinciaSample1();
        Provincia provincia2 = new Provincia();
        assertThat(provincia1).isNotEqualTo(provincia2);

        provincia2.setId(provincia1.getId());
        assertThat(provincia1).isEqualTo(provincia2);

        provincia2 = getProvinciaSample2();
        assertThat(provincia1).isNotEqualTo(provincia2);
    }

    @Test
    void paisTest() {
        Provincia provincia = getProvinciaRandomSampleGenerator();
        Pais paisBack = getPaisRandomSampleGenerator();

        provincia.setPais(paisBack);
        assertThat(provincia.getPais()).isEqualTo(paisBack);

        provincia.pais(null);
        assertThat(provincia.getPais()).isNull();
    }
}
