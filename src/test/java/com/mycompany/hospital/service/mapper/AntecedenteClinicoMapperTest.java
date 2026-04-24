package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.AntecedenteClinicoAsserts.*;
import static com.mycompany.hospital.domain.AntecedenteClinicoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AntecedenteClinicoMapperTest {

    private AntecedenteClinicoMapper antecedenteClinicoMapper;

    @BeforeEach
    void setUp() {
        antecedenteClinicoMapper = new AntecedenteClinicoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAntecedenteClinicoSample1();
        var actual = antecedenteClinicoMapper.toEntity(antecedenteClinicoMapper.toDto(expected));
        assertAntecedenteClinicoAllPropertiesEquals(expected, actual);
    }
}
