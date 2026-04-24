package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.CargoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CargoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cargo.class);
        Cargo cargo1 = getCargoSample1();
        Cargo cargo2 = new Cargo();
        assertThat(cargo1).isNotEqualTo(cargo2);

        cargo2.setId(cargo1.getId());
        assertThat(cargo1).isEqualTo(cargo2);

        cargo2 = getCargoSample2();
        assertThat(cargo1).isNotEqualTo(cargo2);
    }
}
