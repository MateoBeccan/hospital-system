package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.EnfermeroAsserts.*;
import static com.mycompany.hospital.domain.EnfermeroTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnfermeroMapperTest {

    private EnfermeroMapper enfermeroMapper;

    @BeforeEach
    void setUp() {
        enfermeroMapper = new EnfermeroMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEnfermeroSample1();
        var actual = enfermeroMapper.toEntity(enfermeroMapper.toDto(expected));
        assertEnfermeroAllPropertiesEquals(expected, actual);
    }
}
