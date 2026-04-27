package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.EstadoTurnoAsserts.*;
import static com.mycompany.hospital.domain.EstadoTurnoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstadoTurnoMapperTest {

    private EstadoTurnoMapper estadoTurnoMapper;

    @BeforeEach
    void setUp() {
        estadoTurnoMapper = new EstadoTurnoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEstadoTurnoSample1();
        var actual = estadoTurnoMapper.toEntity(estadoTurnoMapper.toDto(expected));
        assertEstadoTurnoAllPropertiesEquals(expected, actual);
    }
}
