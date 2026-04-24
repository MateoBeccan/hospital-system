package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.SexoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SexoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sexo.class);
        Sexo sexo1 = getSexoSample1();
        Sexo sexo2 = new Sexo();
        assertThat(sexo1).isNotEqualTo(sexo2);

        sexo2.setId(sexo1.getId());
        assertThat(sexo1).isEqualTo(sexo2);

        sexo2 = getSexoSample2();
        assertThat(sexo1).isNotEqualTo(sexo2);
    }
}
