package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.EstadoLaboralAsserts.*;
import static com.mycompany.hospital.domain.EstadoLaboralTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstadoLaboralMapperTest {

    private EstadoLaboralMapper estadoLaboralMapper;

    @BeforeEach
    void setUp() {
        estadoLaboralMapper = new EstadoLaboralMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEstadoLaboralSample1();
        var actual = estadoLaboralMapper.toEntity(estadoLaboralMapper.toDto(expected));
        assertEstadoLaboralAllPropertiesEquals(expected, actual);
    }
}
