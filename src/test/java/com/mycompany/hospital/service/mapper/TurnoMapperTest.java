package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.TurnoAsserts.*;
import static com.mycompany.hospital.domain.TurnoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TurnoMapperTest {

    private TurnoMapper turnoMapper;

    @BeforeEach
    void setUp() {
        turnoMapper = new TurnoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTurnoSample1();
        var actual = turnoMapper.toEntity(turnoMapper.toDto(expected));
        assertTurnoAllPropertiesEquals(expected, actual);
    }
}
