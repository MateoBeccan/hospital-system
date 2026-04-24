package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.AntecedenteClinicoTestSamples.*;
import static com.mycompany.hospital.domain.HistoriaClinicaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AntecedenteClinicoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AntecedenteClinico.class);
        AntecedenteClinico antecedenteClinico1 = getAntecedenteClinicoSample1();
        AntecedenteClinico antecedenteClinico2 = new AntecedenteClinico();
        assertThat(antecedenteClinico1).isNotEqualTo(antecedenteClinico2);

        antecedenteClinico2.setId(antecedenteClinico1.getId());
        assertThat(antecedenteClinico1).isEqualTo(antecedenteClinico2);

        antecedenteClinico2 = getAntecedenteClinicoSample2();
        assertThat(antecedenteClinico1).isNotEqualTo(antecedenteClinico2);
    }

    @Test
    void historiaClinicaTest() {
        AntecedenteClinico antecedenteClinico = getAntecedenteClinicoRandomSampleGenerator();
        HistoriaClinica historiaClinicaBack = getHistoriaClinicaRandomSampleGenerator();

        antecedenteClinico.setHistoriaClinica(historiaClinicaBack);
        assertThat(antecedenteClinico.getHistoriaClinica()).isEqualTo(historiaClinicaBack);

        antecedenteClinico.historiaClinica(null);
        assertThat(antecedenteClinico.getHistoriaClinica()).isNull();
    }
}
