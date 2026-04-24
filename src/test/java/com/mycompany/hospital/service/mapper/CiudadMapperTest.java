package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.CiudadAsserts.*;
import static com.mycompany.hospital.domain.CiudadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CiudadMapperTest {

    private CiudadMapper ciudadMapper;

    @BeforeEach
    void setUp() {
        ciudadMapper = new CiudadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCiudadSample1();
        var actual = ciudadMapper.toEntity(ciudadMapper.toDto(expected));
        assertCiudadAllPropertiesEquals(expected, actual);
    }
}
