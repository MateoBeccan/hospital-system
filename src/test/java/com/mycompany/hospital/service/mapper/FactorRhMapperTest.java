package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.FactorRhAsserts.*;
import static com.mycompany.hospital.domain.FactorRhTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FactorRhMapperTest {

    private FactorRhMapper factorRhMapper;

    @BeforeEach
    void setUp() {
        factorRhMapper = new FactorRhMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFactorRhSample1();
        var actual = factorRhMapper.toEntity(factorRhMapper.toDto(expected));
        assertFactorRhAllPropertiesEquals(expected, actual);
    }
}
