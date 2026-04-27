package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.CanalSolicitudAsserts.*;
import static com.mycompany.hospital.domain.CanalSolicitudTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CanalSolicitudMapperTest {

    private CanalSolicitudMapper canalSolicitudMapper;

    @BeforeEach
    void setUp() {
        canalSolicitudMapper = new CanalSolicitudMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCanalSolicitudSample1();
        var actual = canalSolicitudMapper.toEntity(canalSolicitudMapper.toDto(expected));
        assertCanalSolicitudAllPropertiesEquals(expected, actual);
    }
}
