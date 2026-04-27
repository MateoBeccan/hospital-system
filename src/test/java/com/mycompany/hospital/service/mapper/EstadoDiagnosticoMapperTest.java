package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.EstadoDiagnosticoAsserts.*;
import static com.mycompany.hospital.domain.EstadoDiagnosticoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstadoDiagnosticoMapperTest {

    private EstadoDiagnosticoMapper estadoDiagnosticoMapper;

    @BeforeEach
    void setUp() {
        estadoDiagnosticoMapper = new EstadoDiagnosticoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEstadoDiagnosticoSample1();
        var actual = estadoDiagnosticoMapper.toEntity(estadoDiagnosticoMapper.toDto(expected));
        assertEstadoDiagnosticoAllPropertiesEquals(expected, actual);
    }
}
