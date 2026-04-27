package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.EstadoTratamientoAsserts.*;
import static com.mycompany.hospital.domain.EstadoTratamientoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstadoTratamientoMapperTest {

    private EstadoTratamientoMapper estadoTratamientoMapper;

    @BeforeEach
    void setUp() {
        estadoTratamientoMapper = new EstadoTratamientoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEstadoTratamientoSample1();
        var actual = estadoTratamientoMapper.toEntity(estadoTratamientoMapper.toDto(expected));
        assertEstadoTratamientoAllPropertiesEquals(expected, actual);
    }
}
