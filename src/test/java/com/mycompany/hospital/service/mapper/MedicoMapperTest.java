package com.mycompany.hospital.service.mapper;

import static com.mycompany.hospital.domain.MedicoAsserts.*;
import static com.mycompany.hospital.domain.MedicoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicoMapperTest {

    private MedicoMapper medicoMapper;

    @BeforeEach
    void setUp() {
        medicoMapper = new MedicoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicoSample1();
        var actual = medicoMapper.toEntity(medicoMapper.toDto(expected));
        assertMedicoAllPropertiesEquals(expected, actual);
    }
}
