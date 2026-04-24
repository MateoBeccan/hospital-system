package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.TurnoLaboralAsserts.*;
import static com.mycompany.hospital.domain.TurnoLaboralTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TurnoLaboralMapperTest {

    private TurnoLaboralMapper turnoLaboralMapper;

    @BeforeEach
    void setUp() {
        turnoLaboralMapper = new TurnoLaboralMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTurnoLaboralSample1();
        var actual = turnoLaboralMapper.toEntity(turnoLaboralMapper.toDto(expected));
        assertTurnoLaboralAllPropertiesEquals(expected, actual);
    }
}
