package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.FactorRhTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FactorRhTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FactorRh.class);
        FactorRh factorRh1 = getFactorRhSample1();
        FactorRh factorRh2 = new FactorRh();
        assertThat(factorRh1).isNotEqualTo(factorRh2);

        factorRh2.setId(factorRh1.getId());
        assertThat(factorRh1).isEqualTo(factorRh2);

        factorRh2 = getFactorRhSample2();
        assertThat(factorRh1).isNotEqualTo(factorRh2);
    }
}
