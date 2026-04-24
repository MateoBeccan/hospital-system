package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.PaisAsserts.*;
import static com.mycompany.hospital.domain.PaisTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaisMapperTest {

    private PaisMapper paisMapper;

    @BeforeEach
    void setUp() {
        paisMapper = new PaisMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaisSample1();
        var actual = paisMapper.toEntity(paisMapper.toDto(expected));
        assertPaisAllPropertiesEquals(expected, actual);
    }
}
