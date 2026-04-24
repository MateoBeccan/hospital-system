package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.CiudadTestSamples.*;
import static com.mycompany.hospital.domain.ProvinciaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CiudadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ciudad.class);
        Ciudad ciudad1 = getCiudadSample1();
        Ciudad ciudad2 = new Ciudad();
        assertThat(ciudad1).isNotEqualTo(ciudad2);

        ciudad2.setId(ciudad1.getId());
        assertThat(ciudad1).isEqualTo(ciudad2);

        ciudad2 = getCiudadSample2();
        assertThat(ciudad1).isNotEqualTo(ciudad2);
    }

    @Test
    void provinciaTest() {
        Ciudad ciudad = getCiudadRandomSampleGenerator();
        Provincia provinciaBack = getProvinciaRandomSampleGenerator();

        ciudad.setProvincia(provinciaBack);
        assertThat(ciudad.getProvincia()).isEqualTo(provinciaBack);

        ciudad.provincia(null);
        assertThat(ciudad.getProvincia()).isNull();
    }
}
