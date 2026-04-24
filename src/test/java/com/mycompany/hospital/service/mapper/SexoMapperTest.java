package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.SexoAsserts.*;
import static com.mycompany.hospital.domain.SexoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SexoMapperTest {

    private SexoMapper sexoMapper;

    @BeforeEach
    void setUp() {
        sexoMapper = new SexoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSexoSample1();
        var actual = sexoMapper.toEntity(sexoMapper.toDto(expected));
        assertSexoAllPropertiesEquals(expected, actual);
    }
}
