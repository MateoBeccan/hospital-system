package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.TipoEmpleadoAsserts.*;
import static com.mycompany.hospital.domain.TipoEmpleadoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TipoEmpleadoMapperTest {

    private TipoEmpleadoMapper tipoEmpleadoMapper;

    @BeforeEach
    void setUp() {
        tipoEmpleadoMapper = new TipoEmpleadoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTipoEmpleadoSample1();
        var actual = tipoEmpleadoMapper.toEntity(tipoEmpleadoMapper.toDto(expected));
        assertTipoEmpleadoAllPropertiesEquals(expected, actual);
    }
}
