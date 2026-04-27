package com.mycompany.hospital.domain;

import static com.mycompany.hospital.domain.DiagnosticoTestSamples.*;
import static com.mycompany.hospital.domain.EstadoTratamientoTestSamples.*;
import static com.mycompany.hospital.domain.TratamientoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.hospital.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TratamientoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tratamiento.class);
        Tratamiento tratamiento1 = getTratamientoSample1();
        Tratamiento tratamiento2 = new Tratamiento();
        assertThat(tratamiento1).isNotEqualTo(tratamiento2);

        tratamiento2.setId(tratamiento1.getId());
        assertThat(tratamiento1).isEqualTo(tratamiento2);

        tratamiento2 = getTratamientoSample2();
        assertThat(tratamiento1).isNotEqualTo(tratamiento2);
    }

    @Test
    void diagnosticoTest() {
        Tratamiento tratamiento = getTratamientoRandomSampleGenerator();
        Diagnostico diagnosticoBack = getDiagnosticoRandomSampleGenerator();

        tratamiento.setDiagnostico(diagnosticoBack);
        assertThat(tratamiento.getDiagnostico()).isEqualTo(diagnosticoBack);

        tratamiento.diagnostico(null);
        assertThat(tratamiento.getDiagnostico()).isNull();
    }

    @Test
    void estadoTratamientoTest() {
        Tratamiento tratamiento = getTratamientoRandomSampleGenerator();
        EstadoTratamiento estadoTratamientoBack = getEstadoTratamientoRandomSampleGenerator();

        tratamiento.setEstadoTratamiento(estadoTratamientoBack);
        assertThat(tratamiento.getEstadoTratamiento()).isEqualTo(estadoTratamientoBack);

        tratamiento.estadoTratamiento(null);
        assertThat(tratamiento.getEstadoTratamiento()).isNull();
    }
}
