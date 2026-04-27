package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.TipoDiagnosticoAsserts.*;
import static com.mycompany.hospital.domain.TipoDiagnosticoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoDiagnosticoMapperTest {

    private TipoDiagnosticoMapper tipoDiagnosticoMapper;

    @BeforeEach
    void setUp() {
        tipoDiagnosticoMapper = new TipoDiagnosticoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTipoDiagnosticoSample1();
        var actual = tipoDiagnosticoMapper.toEntity(tipoDiagnosticoMapper.toDto(expected));
        assertTipoDiagnosticoAllPropertiesEquals(expected, actual);
    }
}
