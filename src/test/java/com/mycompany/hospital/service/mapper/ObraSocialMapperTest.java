package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.ObraSocialAsserts.*;
import static com.mycompany.hospital.domain.ObraSocialTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObraSocialMapperTest {

    private ObraSocialMapper obraSocialMapper;

    @BeforeEach
    void setUp() {
        obraSocialMapper = new ObraSocialMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getObraSocialSample1();
        var actual = obraSocialMapper.toEntity(obraSocialMapper.toDto(expected));
        assertObraSocialAllPropertiesEquals(expected, actual);
    }
}
